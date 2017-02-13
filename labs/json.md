# Parsing JSON

In this lab, you'll parse a simplified version of JSON. First, familiarize yourself with the structure of JSON by looking at the [syntax diagram](http://json.org).  There is also a pseudo-grammar at that site. Our input will look like:

```
{	"antlr.org": {		"owners" : [],
		"speed" : 100,
		"menus" : ["File", "Help"]
	}
}
```

The first thing you should do is analyze the overall structure of this input, which looks to me like "an object". Inside that object it looks to me like there is "a pair of string : value". But, I can imagine it also allows multiple pairs in an object, separated by commas, because of what I see inside the `antlr.org` object. You also see arrays in square brackets.

At the lexical level, you see strings, integers, whitespace, and punctuation. The punctuation can be easily matched directly using a string literal in the parser rules.

Create a new `JSON.g4` grammar file in intellij that is able to parse the above input. You don't have to create a main program because the goal is to construct a grammar not necessarily call it from Java. Just use the live preview in intellij.

The tree for the above input should look like:

<img src=images/json.png width=450>
