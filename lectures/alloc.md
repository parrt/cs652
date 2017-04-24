# Memory allocation, manual reclamation

Let's say we want to implement C library functions for allocation and manual reclamation of dynamic memory:
 
```c
void* malloc(size_t size);
void free(void* ptr);
```

## Allocation strategies

Paraphrasing [Memory Allocation from University of Illinois](https://courses.engr.illinois.edu/cs241/sp2011/lectures/21-MemoryAlloc.pdf). Find a chunk of memory big enough to hold the requested size and save any left over for a future allocation. 

The goals are speed of allocation, deallocation, and reduced *fragmentation*. It's possible to have plenty of space available to service an allocation size but not in a single contiguous chunk.

1. First fit. Creates average size holes.
2. Best fit; exact size or smallest chunk that works. Smallest left over holes but creates lots of small holes that can't really be used.
3. Worse fit; use largest available chunk. Produces the largest left over holes but can't run large programs.

To reduce fragmentation, we can perform compaction but it means updating all of the pointers within the fields of objects allocated already in the heap as well as altering a pointers from the program into the heap. Or, we could use a [handles](http://www.brpreiss.com/books/opus5/html/page429.html) mechanism whereby all pointers are assumed to be pointers to pointers. We can update an objects location without having to change any of the "pointers" to that object.

There are two fundamental allocation strategies:

* *sequential* (*bump pointer allocation*)
* *free-list allocation*

## "Gift" allocator

What's the simplest way to implement? `malloc` just gets more memory from the operating system and `free` is a no-op. hahaha. Oddly enough this works for many short-lived applications; in fact, most applications probably have memory leaks because they forget to call `free` so in fact they might be using this methodology without knowing it.

This uses a sequential allocation strategy because it just gives away more memory at the so-called high watermark. This is sometimes called bump pointer allocation.

Bump pointer allocation is extremely efficient for allocating memory.

## "Loan" allocators (we want it back)

### Stack allocator

The simplest and most efficient memory allocator is always a bump pointer allocator, but reclaiming memory can be challenging. If we allocate and deallocate memory and stack fashion, then we can use the call stack. In other words, local variables are very efficient for allocation and get automatically deallocated (reclaimed). For example,

```c
void f() {
	int a[] = {1,2,3,4,5}; // allocate 5 ints on stack
	...
	// a[] is automatically reclaimed at the end of function
}
```

Of course, this doesn't work when we need to build up a data structure and return it because that memory space goes away upon return. Any references to it would be *stale*.

Java has no mechanism to allocate anything beyond primitives and object references on the stack, but C++ allows local objects and arrays.

### Getting memory from OS

Use `mmap` to get a chunk of memory from the OS and `munmap` to give it back. The following routines are used to manage memory and big chunks as part of `malloc`/`free` implementations. 

```c
/* Allocate size_in_bytes memory (not mapping a file to this memory...MAP_ANON).
 * This routine is independent of any headers needed by malloc() implementations.
 * Add any extra memory needed.  This is intended for educational purposes to
 * ask the OS for a heap of memory for malloc() and GC implementations.
 */
void *morecore(size_t size_in_bytes) {
	void *addr = 0;
	const int filedescriptor = -1; // not mapping a file to memory
	const int offset = 0;
	void *p = mmap(addr, size_in_bytes, PROT_READ|PROT_WRITE,
				   MAP_PRIVATE|MAP_ANON, filedescriptor, offset);
	if ( p == MAP_FAILED ) return NULL;
	return p;
}

/* Note: man page says size_in_bytes must be multiple of PAGESIZE but it seems to work
 * with non page sizes.
 */
void dropcore(void *p, size_t size_in_bytes) {
	if ( p!=NULL ) {
		int ret = munmap(p, size_in_bytes);
		assert(ret == 0); // munmap returns non-zero on failure
	}
}
```

For example, to create a heap, do

```c
static void *heap = NULL;	    // point to data obtained from OS
...
heap = morecore(max_heap_size);
```

### Free list

Allocators often use a *free list* which we can think of as just a linked list of free chunks of memory within the heap. (Technically the free list is a set not perforce a list.) The list could be external to the heap or threaded within the object to the heap itself. In fact it doesn't have to even be a list. [Doug Lea's malloc](http://g.oswego.edu/dl/html/malloc.html) uses size information at the start and end of every allocated or free block. (He calls them *boundary tags*.) Traversing the list is a matter of hopping over chunks rather than directly jumping to a pointer. It also means we can easily coalesce neighboring free chunks into a single larger free chunk.

Maintaining a free list as a linked list requires two pointers:

```c
static Header *freelist;   // Pointer to the first free chunk in heap
static void *heap = NULL;       // point to data obtained from OS
```

where the `freelist` is initialized to be a single chunk of memory starting at `heap`.

The allocated chunks of memory have a bit of overhead called a *header* that tracks the size of the overall chunk, including the header, a status bit indicating allocated or not allocated, and a next pointer that is hooks free chunks together.

```c
typedef struct _Header {
    uint32_t size;     // 31 bits for size and 1 bit for inuse/free; includes header data
    struct _Free_Header *next;
    unsigned char mem[]; // nothing allocated; just a label to location after size
} Header;
```

Here is what the initialized heap looks like:

<img src=images/heap-init.png width=250>

The `next` pointer is unused if the chunk is allocated and in use by the user program.

Here is what the heap looks like after a single allocation:
 
<img src=images/heap-after-alloc.png width=190>

If the user gives that chunk back, then the heap looks like this:

<img src=images/heap-after-free.png width=210>

Naturally having the `next` pointer in the header even for allocated chunks is a waste of a 4 or 8 byte pointer. So, typically you will see two different headers, one for a chunk in use:

```c
typedef struct _Busy_Header {
    uint32_t size;     // 31 bits for size and 1 bit for inuse/free; includes header data
    unsigned char mem[]; // nothing allocated; just a label to location after size
} Busy_Header;
```

and one for a chunk on the free list:
 
```c
typedef struct _Free_Header {
    uint32_t size;
    struct _Free_Header *next; // lives inside user data area when free but not when in use
} Free_Header;
```

### Binning

[Lea's malloc]() uses bins whereby "*available chunks are maintained in bins, grouped by size. Bins for sizes less than 512 bytes each hold only exactly one size. Searches for available chunks are processed in smallest-first, best-fit order.*"

<img src=http://g.oswego.edu/dl/html/malloc2.gif width=500>

The space within free chunks has pointers that form a linked list within a bin of the appropriate size. This indicates there is a minimum size for any allocated chunk but it's no big deal. 

See [lecture 12 from Harvard CS61: Systems Programming and Machine Organization](http://www.eecs.harvard.edu/~mdw/course/cs61/mediawiki/images/5/51/Malloc3.pdf) for more details; e.g.,

<img src=images/dlmalloc-bins.png width=450>

For the bins that do not have exact sizes, above 512, you can sort the elements within the bin for faster searching.

### Bitmaps

Instead of a free list, allocators can use a bitmap. From "*Mark without much Sweep Algorithm for Garbage Collection*" by Danko Basch, Dorian Ivancic, Nikica Hlupic:

<blockquote>
Allocators may use bitmaps where each bit is mapped to several bytes in the heap. Each bit denotes whether its associated bytes are free or used. On allocation request the allocator scans the bitmaps in search for sufficiently long sequence of zeroes. To free the block, all its related bits should be cleared so the block size has to be known.
</blockquote>

From (I think) *Tanenbaum & Woodhull, Operating Systems: Design and Implementation, (c) 2006*:

<img src=images/bitmaps.png width=500>

## Comparison

It is my understanding that bitmaps require more memory than linked lists but that bitmaps might be faster for deallocation because they can just set some bits rather than having to coalesce adjacent free regions and so on. On the other hand, the free list should be faster for allocation, particularly with binning.  An update Fall 2015, Hanzhou Shi tested bitmap, bytemaps and found they were *much* slower than simple linked-list strategies.