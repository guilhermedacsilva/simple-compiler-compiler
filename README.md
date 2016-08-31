br.gui.cc
=========

Compiler Compiler (writen in Java)

This is a project for study compilers.

Scanner generator (br.gui.cc.lexer)
-------------
Sample configuration input:

	integer
	^\d+$
	
	float
	^\d+\.$
	^\d+\.\d+$
	
	comment
	^/\*.*\*/$
	
	plus
	^\+$

Explanation: first line is the token class (or type). Second line is the regex to match to token.
The regex must be a regular Java pattern.
It will try to get the longest lexeme. In case there are more than 1 regex, only the last one is the complete regex.
The others will be used just to match partial. Match partial is interesting when one regex is prefix of another.

Parser generator (br.gui.cc.parser)
-------------
Sample configuration input (try use only letters):

	expression number operator number
	number integer
	number float
	operator plus

Explanation: it is like BNF. The first word of the line is the LHS (nonterminal). The rest is the RHS. The whitespace is the separator.
The RHS can be terminal or nonterminal.

Special symbols: + ? *
They can only appear in the end of a nonterminal.
Example:

	expression number operator number more*
	more operator number
	number integer
	number float
	operator plus

Another special word is epsilon. It means an empty production.

There are 3 different parsers: Recursive Descent, LL1 Predictive Parser and SLR Parser. Each one is in their package.
The usage is the same, because all implement the DynamicParser interface.
To read a file and convert it to parser configuration objects, use the classes in package br.gui.cc.parser.reader.

Semantic generator (br.gui.cc.semantic)
-------------
TODO =/
