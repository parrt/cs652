# Symbols and scopes

Carefully consider the following Java code.

1. Circle all of the symbols.
2. Draw arrows from symbol references to their definition site (if it exists within this file).
3. Identify the named scopes
4. Identify the anonymous scopes

```java
// Adapted from http://swerl.tudelft.nl/twiki/pub/Main/TechnicalReports/TUD-SERG-2012-015.pdf
package a.b;

import u.Y;

class X extends Y {
	int f(int x) {
	    int x,y;
	    { int x; x - y + 1; }
	    x = y + 1;
	}
}

class Z {
    class W extends X { 
        int x;
        void foo() { f(34); }
    }
    int x,z;
    int f(int x) {
	     int y;
	     y = x;
	     z = x;
    }
}
```