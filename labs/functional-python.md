# Homework: Fun with Python

*To submit your answers, please work out all of the solutions and then run them as a single script through the Python REPL. Please print that run out and turn it in on paper. For example, I would like to see sequences like:*

```python
>>> L = [5,9,2,100,41]
>>> sum(L)
157
...
```

## Lambdas

1. Define the equivalent of the following function as a lambda
```python
def rshift(x):
	return x >> 1
```

## Map/reduce

1. Map that lambda from the previous section across `L = [5,9,2,100,41]` with `map()`.
1. Use `filter()` to return the list of elements in `L` less than 40.
1. Use `reduce()` to multiply all the elements together.

## Iterators

1. Write an iterator class that, given a string via the constructor, returns a sequence of characters.  It should return 1 character for each `next()` invocation. Tested out with a `for` loop that prints out the characters for string "hello".

## List comprehensions

1. Repeat the map,filter,reduce operations from the map/reduce section but using list comprehensions. You will get three new expressions for this part.

Now, assume the following data:

```python
L = [143,98,4]
names = ['parrt', 'ksb', 'tombu']
```

1. Using a list comprehension, create a list containing the words from `names` whose length is greater than 3.

1. Create a list containing all uppercase versions of the elements in names array.
1. Create a list of tuples with the upper and lower case versions like
`[('PARRT','parrt'), ('KSB','ksb'), ...]`.   You will have to look up how to do this as we did not go over it in class. It is easy and fun, however.

## Higher-order functions

1. Write your own `map(fun,list)` function that takes a function and a list or other sequence as arguments and then returns a list with `fun` applied to each element of list.
```python
def map(fun, list):
	...
```
1. Write a function `const(x)` that returns a function that always returns x.
```python
def const(x):
	...

one = const(1)
print one() # 1
print one() # 1
two = const(2)
print two() # 2
```