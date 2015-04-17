# maikurokanren

maikuroKanren (or マイクロKanren) is an implementation of the µKanren relational programming language defined in [this paper](http://webyrd.net/scheme-2013/papers/HemannMuKanren2013.pdf) by Hemann & Friedman.

It is implemented in the Clojure programming language.

## Current State

This solution works up until the description in the paper for managing infinite streams. I got a bit stuck there - my solution doesn't work. There is a bug in the Clojure code. I could fix it, but, I think I'm going to continue to keep getting silly bugs due to my messy solution.

I would like to reimplement this from scratch, here are some notes for future Chris:

* Read the entire paper and understand the terms before writing any code
* Write idiomatic Clojure - not a combination of kind of Scheme, Clojure and something in between
* Use longer or more descriptive names.
