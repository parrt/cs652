# Type compatibility in OO languages

Given classes:

```
class T {...}
class A extends T {...}
class E extends T {...}
``` 
Which of the following assignments will compile in Java (put yes or no next to each line)?

```
A a;
T t;
E e;
a = t;	______
t = a;	______
e = t;	______
t = e;	______
a = e	______
e = a;	______
```