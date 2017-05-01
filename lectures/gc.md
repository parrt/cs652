# Memory allocation and garbage collection

## Introduction

For much of my career (prior to 1995 when Java appeared on the scene), debugging software was all about answering a frustrating, but critical question: "is there a problem with my algorithm or have I corrupted memory and, hence, the runtime system?" I have traced problems down to memory allocation / deallocation issues many many times. I used to build special memory.h that left magic numbers before/after objects etc...

Fortunately, software can help us write software. Just as we use IDEs and debuggers to make us more efficient, we can use automatic garbage collection (GC) to avoid dynamic memory programming errors and increase productivity dramatically. You are not good at tracking garbage manually and programmers typically do lots of extra copying to resolve "who frees what" issues between programmers.

Simply put, GC relieves the programmer from having to track and deallocate dynamic memory--you do not have to write code to deallocate data structures. GC reduces cognitive load. People estimate about 10% of CPU time for automatically collecting garbage. An excellent trade.


### The basic idea and terms

The core GC strategy is:

1. At some frequency, distinguish live from dead objects (tracing) and reclaim dead ones.
1. Live objects are reachable from roots, which are globals, locals, registers of active methods etc...

**Analogy**: Imagine walking to the refrigerator and getting out a bowl of grapes. You pick up the bunch by the stem and look in the bottom of the bowl; there are a bunch of black and blue moldy grapes--that's the "garbage". Anything not reachable from the stem has gone "bad." (This analogy is attributed to Randy Nelson, now at Pixar, formerly of the Flying Karamazov Brothers).

Imagine a heap of dynamic memory for a running program. Certain variables, *root pointers*, point into the heap to your data structures. Roots are any variables a program can access without indirecting through a pointer; this includes global variables, parameters, local variables typically. I think of them as any pointer that lives outside the heap that points into the heap.

Anything *reachable* from a root is considered *live data*. When those variables no longer point at a data structure, nothing can reach the data structure so it's garbage:

![gc before](images/gc1.gif)

After garbage collecting, you'd see something like this:

![gc before](images/gc2.gif)

GC often implicitly assumes that the objects in memory are typed so you know how to find the pointers within, say, a parse tree node. This does not mean that GC only works in interpreters--compiled languages such as C++ can store runtime type information now.

Further, w/o type information you can still do *conservative collection*. If it looks like a pointer, assume it is. That unfortunately leaves some objects around because you still (erroneously) think you have a pointer to those objects (in fact they are probably just integers). Java has lots of type information in the objects as well as the bytecodes that operate on data, hence, does not have to be conservative in general. Conservative collection is bad in my opinion since "conversative GC implies leaking memory". The opposite of conservative collection is a *precise collector*.

[Important terms defined](http://www.iecc.com/gclist/GC-algorithms.html)

## Common Strategies

There are two main user-perspective categories of GC: *disruptive* and *nondisruptive* (often called *pauseless*). I can remember LISP programs halting in the middle and saying "Sorry...garbage collecting...". Ack! A disruptive GC is one that noticeably halts your program and usually means it's doing a full collection of a memory space and literally turns off your program for a bit. If you interleave little bits of collection alongside the running program, you call it an *incremental collector*. If it runs at the same times as the program, you call it a *concurrent* collector. These collectors are a lot harder to implement because you must deal with the program altering data structures while the collector sniffs the structures. If you are building a real-time system, however, incremental collectors are pretty much a requirement.

### Reference Counting

This simplest GC strategy is [reference counting](http://www.brpreiss.com/books/opus5/html/page421.html), which adds a counter for every "object" in your system. When you copy a reference to that object, you increment the count by one. When a reference to an object goes away (such as when a local variable goes out of scope), the count is decremented by one. If, at that time, the count goes to 0, the object is garbage and all pointers emanating from it should be decremented. Then that object is reclaimed (as are any other objects that go to 0 during the count update phase).

Reference counting is mostly nondisruptive, but can't handle cycles. A cycle looks like this:

![cycle before](images/gc-cycle1.gif)

and then after you dereference the object and decrement its count, it is still greater than 0 because an object inside the heap refers to it. These objects are no longer reachable from a root and will never be reclaimed. Swift has this cycle issue. See [Resolving Strong Reference Cycles Between Class Instances](https://developer.apple.com/library/content/documentation/Swift/Conceptual/Swift_Programming_Language/AutomaticReferenceCounting.html#//apple_ref/doc/uid/TP40014097-CH20-ID52).

![cycle before](images/gc-cycle2.gif)

Cycles occur often enough: as in circular queues, doubly-linked trees etc...

The cost is also high as it is proportional to the amount of work done in program.

Atomicity of

```c
p = ...;
p->ref++; // bump reference count of p's target object
```

and

```c
p->ref--;
if ( p->ref==0 ) free(p);
p = ...; // must dereference object pointed to by p before reassignment
```

operations must be atomic threaded environments, which can hurt performance due to locking.

### Disruptive, Stop-And-Collect Schemes

#### Mark and sweep

[Mark and sweep](http://www.brpreiss.com/books/opus5/html/page424.html) collectors are two-phase collectors that first walk the live objects, marking them, and then finds all the *dead objects* (i.e., anything that is not live). This is pretty easy to understand and build but badly fragments memory, wastes time walking dead objects (assuming you don't have to run destructors), and has bad virtual memory characteristics.

Allocation typically requires a free list implementation, similar to what we saw for `malloc`/`free`.  We allocate objects until we run out of memory in the heap and then perform a collection:

```c
void gc() {
    mark();
    sweep();
}
```

Every root, pointer into the heap, must be tracked by the collector. Code that uses the garbage collector must announce roots to the collector. We can either track the pointer or the address of the pointer so that we can change the roots if a collector shuffles objects around in the heap:

```c
static heap_object **_roots[MAX_ROOTS];
```

For example, after the heap gets highly fragmented, we can perform a compaction, which necessarily moves all of the roots.

**Marking**.  To mark an object, we either have to add a mark a bit to each object's header or keep a separate table that maps objects to a mark bit. The latter is preferable for efficiency reasons of the collector itself and it does not require alterations to the objects. This could be important with uncooperative languages like C or C++ where we cannot intrude on their layouts. Each bit in a bitmap denotes the possible starting locations for objects.

For simplicity, we can think of implementing objects (using C) with the following header:

```c
/* stuff that every instance in the heap must have at the beginning (unoptimized) */
typedef struct heap_object {
    struct _object_metadata *metadata;
    uint32_t size;      // total size including header information used by each heap_object
    bool marked;        // used during the mark phase of garbage collection
    struct heap_object *next; // used by free list
} heap_object;
```

To mark, we follow this algorithm:

```c
void mark() {
    for each root in roots {
        mark_object(root)
    }
}

void mark_object(heap_object *p) {
    if ( !p->marked ) {
        p->marked = true;
        gc_chase_ptr_fields(p);
    }
}

void gc_chase_ptr_fields(heap_object *p) {
    for each ptr field of p { // determined from the metadata
        if field!=NULL {
            mark_object(field)
        }
    }
}
```

Chasing pointers among the objects means knowing the offset of all pointer fields of the various objects:

```c
typedef struct _object_metadata {
	char *name;               // "class" name of instances of this type; useful for debugging
	uint16_t num_ptr_fields;  // how many managed pointers (pointers into the heap) in this object
	uint16_t field_offsets[]; // list of offsets base of object to fields that are managed ptrs
} object_metadata;
```

**Sweeping**.  After marking, our collector walks the heap, jumping from object to object using the object sizes stored in the headers. This is a *heap hop*. If the object is marked, we unmark it. If the object is not marked, then it is dead and we add it to the free list.

```c
void sweep() {
    p = start_of_heap;
    while ( p < end_of_heap ) {
        if (p->marked) {
            unmark_object(p);
        }
        else {
            free(p);
        }
        p = p + p->size; // jump to the next object
    }
}
```

The complexity is on the order of the number of objects in the heap and we must walk even the dead stuff just free it.

#### Mark and compact

A proposed improvement to the mark-and-sweep strategy is called [mark and compact](http://www.brpreiss.com/books/opus5/html/page428.html). It is called a compacting collector because it walks memory moving all live objects to the front of the heap, thus, leaving a nice big contiguous block of free memory afterwards. This removes fragmentation concerns and helps locality for cache / virtual memory because objects are kept in same order in which they were allocated. We still have to walk the memory a lot and you have to update pointers since you are moving things around. We pack all live objects at the start of the heap, which effectively overwrites all of the dead stuff. To assign new locations for the live objects, we have to walk the objects in **sorted address order** to shift all objects "down" in the heap.  We don't have to sort though.  We can just "heap hop" from the start of the heap, hopping by `p->size`. That means that we have to "touch" even the dead objects as we look for the live ones. Alternatively, we could keep an external list of live objects, doing an insertion sort during the Mark phase.

**Analogy**. Mark and compact behaves like shuffling students around in a dorm at the end of a semester. Some students will stay around for summer school and we would like to shift all n of these "live" students down to the first n dorm rooms.
First, we assign new dorm rooms to all of the live students and tell them their "forwarding address", which they must keep track of. Next, we ask each student to update their contact list for the live students (point at forwarding address). The roots might be analogous to a student's mother, who must now refer to her child at the new address. Finally, we move all of the students to their assigned new room. 

One of the big advantages of the mark and compact collector is that it supports bump pointer allocation, which is extremely fast.

```c
void *gc_raw_alloc(size_t size) {
	if (next_free + size > end_of_heap) {
		gc(); // try to collect
		if (next_free + size > end_of_heap) { // try again
			return NULL;                      // oh well, no room. puke
		}
	}

	void *p = next_free; // bump-ptr-allocation
	next_free += size;
	return p;
}
```

Much much faster than the free list implementations. On the other hand, the compact operation is fairly complicated and typically uses 3 passes and requires (in one mechanism) an extra forwarding_addr field in each object:

```c
/* stuff that every instance in the heap must have at the beginning (unoptimized) */
typedef struct heap_object {
    struct _object_metadata *metadata;
    uint32_t size;      // total size including header information used by each heap_object
    bool marked;	    // used during the mark phase of garbage collection
    struct heap_object *forwarded; // where we've moved this object during collection
} heap_object;
```

After walking the object graph to mark live objects, we perform the following.

1. Walk all live objects, p, in address order and compute their forwarding addresses starting from start\_of\_heap (bumping a pointer). 
2. Walk all live objects, p, alter all non-NULL pointer fields of p to point to the forwarding addresses. Alter all non-NULL roots to point to the object's forwarding address. 
3. Physically move object each live object to its forwarding address towards front of heap and unmark it. This phase must be last as the object stores the forwarding address. When we move, we overwrite objects and could kill a forwarding address in a live object.

If we keep the forwarding address inside the objects themselves, we need 3 passes. More specifically, we need to keep #3 separate and keep it after the others. We cannot move objects until all pointers have been updated.

By computing all new addresses and holding them in an area outside of the heap, with marked bits or separate temporary live list, we can reduce the number of passes to two. We can move objects and set pointers at the same time.  If we don't keep forwarding addresses within the objects themselves, we need a map from old to new addresses, which can be expensive in space and time if we're not careful. 

I believe that we always need to compute all forwarding addresses first. If we tried to move objects without looking at all objects, we might clobber a live object. Imagine a live object sitting at the first memory location in the heap and imagine that we visit it last. At least one object would be stepping on top of it.

While memory allocation is much faster with mark-and-compact, it appears to the slower in general than mark-and-sweep because of the extra passes over the objects in the heap. Also, each pass tends to be expensive. Resetting all those pointers is expensive. A compromise is to mark-and-sweep and then occasionally compact.

One thing to notice about these compacting collectors: long-lived objects tend to cluster at the start of the heap. One can simply skip over these during collection to improve performance.

#### Scavenging (copying) collectors

If instead of moving live objects to the head of a single heap, you copy live objects to another memory space, you have a copying collector. You only have to walk the live objects (updating their pointers as you move them)--anything left in the old space is garbage. The term *scavenging* is often used to refer to this process. This has the advantages of the mark and compact algorithm, remove fragmentation and bump pointer allocator, but is easier to implement and can perform better. We simply recursively move objects and update pointers as we traverse live objects. As with other schemes moving objects, we have to track forwarding addresses either in the object itself or as a separate data structure. And advantage of a copying collector over Mark and compact is that we don't need a busy bit. The forwarding address can be overlapped with space outside the header in each object, at least after we have copied that object into the other space.

A super awesome mechanism for walking the live objects is called *Cheney scanning*. This *breadth-first* algorithm first copies all objects pointed to by roots to the target space. The set of object field pointers then consists of all pointers we might need to walk and, since the objects are consecutive in memory, we don't need a linked list or separate array to track them. We can simply hop from object to object in the target space looking for pointers back into the source space.  Here is an illustration from the excellent [The Garbage Collection Handbook](http://gchandbook.org/):

<img src=images/cheney-scanning.jpg width=400>

Or, here's a sequence from a simple heap that copies from a source to target:

<img src=images/gc-copying1.png width=450>
<img src=images/gc-copying2.png width=450>
<img src=images/gc-copying3.png width=450>
<img src=images/gc-copying4.png width=450>

When we copy an object from the source to the target space, that object in the source space becomes what I call a "zombie", because it is sort of undead. We aren't going to use it anymore as an object but we can't get rid of it yet because we store the forwarding address in that object.
 
Allocation for any copying collector is fast because you just have to bump a pointer in the heap; all free memory is contiguous after collection.  The cost is that we can only use up to half the memory available because we have two spaces.  Also copying collectors have a lot of work to do moving objects at each collection!  Note that if you have a finalize() method (a destructor), it implies you have to walk garbage even if not strictly required by your strategy.

### Details of a simple recursive implementation

A scavenging collector operates on two equally sized heaps, heap0 and heap1. We ping-pong between heaps by swapping heap0 and heap1 pointers. That way we can always be scavenging from heap0 to heap1. 

At each collection, all live objects are scavenged from one space and copied to the start of the second space. The roots are updated and we flip from one heap to the other. Scavenging leaves the current heap, heap0, completely empty.  New allocations (after collection) are done in target heap at the address beyond all of the objects that we copied from heap0. In other words, if we scavenge 1000 bytes from heap0, new allocations in heap1 occur at address 1000. Because we swap heap0 and heap1 at the end of collection, however, new allocations (by the user program) always occur in heap0.

We have:

```C
static void *heap0;
static void *heap1;
```

plus the pointers we used in mark and compact:

```C
static void *start_of_heap;
static void *end_of_heap;
static void *next_free;
static void *next_free_forwarding;
```

`end_of_heap` can be easily computed on demand. `next_free` and `next_free_forwarding` operate in `heap0` and `heap1`, respectively.

Scavenging does not require a mark bit field or a forwarding address field in live objects. However, we do need a forwarding address field in zombie objects. A zombie object is one that has been copied into the new space but before the end of the collection process. Zombies hang around for the sole purpose of updating pointers from other objects (and roots) to the forwarding address in heap1. For our purposes, perhaps it's best to just force every object to have a forwarding address field.

The algorithm can be done recursively, and all in one function, without additional data structures. It is in some ways simpler than the mark and compact due to lack of freelist, at the cost of twice as much memory.

```C
heap_object **_roots[MAX_ROOTS];
For each root {
	*_roots[i] = _forward(*_roots[i]);
}
```

```C
heap_object *forward(heap_object *p) {
    // first check to see if we have already processed this object
    if p address in target heap1, return p; // p points to real obj in heap1
    if p->forwarded, return p->forwarded;   // p is a zombie in heap0, real in heap1
    p' = next_free_forwarding;              // bump allocate in heap1
    next_free_forwarding += p->size;
    p->forwarded = p';                      // zombie knows real location
    copy p to p';                           // copy obj from heap0 to heap1
    for each pointer field f of p {
        f = forward(f);         // move objects reachable from the old
    }                           // and update pointer field
    return p';                  // return new location in heap1
}
```

### Nondisruptive, Generational Schemes

The disruptive stop-and-collect schemes are so disruptive because they have so much work to do--they must deal with the entire heap. If your heap is 8G, then it has lots to do. With the mark and copy collector, we are constantly moving long-lived objects from one heap to the other and back. This is a huge waste of time.

**Observation:** most objects live only a short time while some tend to live a long time (think about `System.out` object in Java).

A *generational collector* takes advantage of this observation by having a "younger" and an "older" generation. Objects that live a few "generations" (i.e., collection runs), are moved to the "older" generation, which reduces the amount of live objects the collector must traverse in the younger generation.

[Myths and Realities: The Performance Impact of Garbage Collection](http://www.cs.utexas.edu/users/mckinley/papers/mmtk-sigmetrics-2004.pdf): "*Our experiments show that the generational collectors provide better performance than the whole heap collectors in virtually all circumstances*."

Note the similarity to the mark and copy algorithm; here, though, the "when to copy" algorithm is very different. A generational copying collector moves objects to another generation when it has survived a few generations. Mark and copy copies all live objects upon each activation, thus, not significantly reducing its workload for future generations. Also, there may be many generations, not just two spaces as in a mark and copy scheme. A generational collector is not wasting half its heap either.

Generational collectors have more bookkeeping to do than the mark and compact or mark and copy collectors. They have track references that span generations. In general, all or most pointers will point intra-nursery or into older generations and not point from older generations into the nursery. Hopefully that means our overhead is not too bad.

We want to reduce how many live objects we have to look at to determine what is live and what is dead and so we want to trace only the objects in the nursery. But, we might find that there are objects pointed to from the older generation but not from the nursery itself. We can't declare such objects as dead.  That means we need a way to find pointers into the nursery from the older generation without having to walk all live objects in the older generation.  We might conjure up pointers from the old into the nursery when we move objects to the older generation and the mutator itself can alter pointers. We need to track memory writes to identify pointers that point into the nursery from the old generation. These are oddly called *write barriers*.  A write barrier is literally some code generated by the compiler upon each pointer store. In both cases, the *remembered set* records the address of pointers that contain intergenerational pointers of interest.  This effectively increases the size of the root set for doing the minor collection in the nursery. See [Slava Pestov's write barrier discussion](http://factor-language.blogspot.com/2009/10/improved-write-barriers-in-factors.html).

See [Myths and Realities: The Performance Impact of Garbage Collection](http://www.cs.utexas.edu/users/mckinley/papers/mmtk-sigmetrics-2004.pdf):

<blockquote>
We carefully measure the impact of the write barrier on the
mutator and find that their mutator cost is usually very low (often
2% or less), and even when high (14%), the cost is outweighed by
the improvements in collection time.
</blockquote>

In the end, even generational schemes must stop-and-collect the older generations. Hopefully this can be hidden from the user such as when the system is waiting for user input. Java does this by making the collector the lowest priority thread. When nothing else is running, the collector starts up (and hopefully finishes).

### Nondisruptive, Incremental Tracing Collectors

If you must avoid stopping to do collection, you can interleave collection with the running program, stopping the program during tiny collections, with an *incremental collector*.  Or you can interleave the GC work with the execution of the *mutator* (the program) with a *concurrent collector* that does not stop the execution of the mutator. It presents an obvious concurrency problem because, while it's looking for (tracing) live objects, the mutator can rearrange the objects.

Important fact: once something is garbage you can never become live again. A concurrent collector can therefore remove any garbage it identifies even though the mutator is currently changing pointers all over the place.

Hard real-time systems use incremental collectors--you need fixed-cost GC operations.

The sum of smaller incremental collections may be greater than the cost of one big collection.

## Handles

Moving objects means altering pointers, not only the roots but all pointers within objects on the heap that point to other objects on the heap.  To move an object on the heap, we have to alter all pointers that point and it. One way to have a single pointer changes to use [handles](http://www.brpreiss.com/books/opus5/html/page429.html).

## Pointer/Root identification

To perform garbage collection, we need to know what the roots are...the pointers from outside the heap into the heap. These pointers are global variables, parameters, and locals. We also need to know how to identify pointer fields of objects if we are not using handles because we would need to chase them to trace through the live objects.

The first problem is identifying the roots. One way is to trace the entire runtime stack looking for values that could be pointers (leading to a *conservative* collector). Sometimes an integer will masquerade as a valid pointer. That might lead us to conclude that an object is live when in fact it is dead. And also require special knowledge of the runtime stack. This might be okay in a managed language like Java, but is very operating system and architecture specific for languages like C.  It's also hard to ask if a pointer is valid depending on the environment.  If we are managing the heap, we can often determine whether a pointer is valid by comparing it to the start and stop of our heap space(s).

If we are using direct pointers to implement pointers in our source language, we need to know what offsets they are within instances of each object type. For example, it's hard to tell from a simple C declaration what the field offsets are, even if the compiler tries to word align fields:

```c
struct {
	char a;
	char b;
	char *p; <-- the offset of this field is compiler specific
}
```

That way we can trace through all of the objects looking for live objects. The heap might have pointers to all objects, but we need to trace them to separate the living from the dead.


## Resources

Paul Wilson's [Uniprocessor Garbage Collection Techniques](wilson-gc-overview.pdf)

[Garbage Collection: Algorithms for Automatic Dynamic Memory Management](http://www.amazon.com/Garbage-Collection-Algorithms-Automatic-Management/dp/0471941484)

[U Texas Memory Management course](http://www.cs.utexas.edu/users/mckinley/395Tmm/schedule.html)
