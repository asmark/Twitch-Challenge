Twitch-Challenge
================

Problem specification
---------------------
http://www.twitch.tv/problems/spellcheck


How to run
----------
To run the spellchecker: `make run`  
To run the word generator: `python wordgenerator.py [words.txt]`

Dependencies
------------
* python 2.7
* java 7 (6 should also work)
* GNU Make 3.81

Solution description
--------------------
We begin by transforming all of the valid words into a normalized form. Here we normalize case and convert vowels into wildcards. Hence the string "hello" becomes "h*ll*". These normalized words are stored in a trie. The decision to use a trie rather than a dictionary is important and will be explained in the caveats section.  

When conducting a spell-check we also normalize the input word in the same way as before, except we truncate duplicated letters as well. Searching for "ppppphone" is first normalized as "p+5+h*n*". 

We then iterate through the trie attempting to match the strings. Special care is taken in the case where the input string contains duplicated letters. We illustrate with an example:  
hollo could be a mispelling of either "hole" or "hello". Therefore, we must examine the cases where l is repeated once, or twice separately. The trie, then, acts as a state machine, where we must keep track of multiple states.

Caveats
-------
I initially thought that a dictionary would suffice instead of a trie. However, there is a caveat when it comes to the repeated letters clause.  
Consider the word: giddy. I thought this could be normalized as g*d*, and therefore "gidddy", "giddi", etc. would all match when normalized.   
However, we have an undesirable match "gidy". Therefore, we must preserve duplicated letters in the dictionary, as the absence of letters does not qualify as a valid mispelling.
The trie allows us to verify a certain number of letters before progressing to the next state.


Runtime
-------
Constructing the trie takes linear time and space in the amount of words to construct, multiplied by the average length of a word.

However, the analysis for finding matches is a little trickier.
As mentioned previously, we must keep track of multiple states at once in the case of repeated letters.
Consider the prefix "aaa". We must consider cases:
* a is repeated 3 times (ie. [aaa]ple)
* a, followed by a repeated twice (ie. a[aa]rdvark)
* a is not unecessarily repeated (ie. if AAA were a valid word)

As we proceed down the trie, we simultaneously visit neighbours of "a", "aa", and "aaa". Therefore, the maximum number of states we can currently be in is in the order of the amount of repeated letters. 

Therefore, given a word of length n, the worst-case runtime is when all n letters are the same. After all n iterations, we will be n states simultaneously. Given an constant amount of time per state checked, we have the familiar series 1 + 2 + 3 + ... + n = O(n^2) time needed.

Of course, this is the case with a general dictionary. In reality, english dictionaries do not contain many repeated letters, and so the search domain is pruned; we do not unecessarily visit states that have no valid words.

Extensibility
-------------
This solution can correct more class of problems by adding another rule in the spellcheck.rule package. Special care must be taken, however, to ensure it does not interfere with the processing of other rules. Ordering of rules may matter here, and so this is not an ideal solution.

Retrospective
-------------
I used a non-deterministic finite automata (without epsilon transitions) to model the spellcheck dictionary. When traversing through (incorrectly) repeated letters, this caused the spellechecker to keep track of multiple states at once. This is far from ideal. 

A better approach would be to use a deterministic finite automata. This would cut the traversal runtime to linear in the length of the input, since each state in a DFA is modelled by a group of states in an NFA. The drawbacks are, of course, that the transformation of a NFA->DFA can be exponential in the worst case.

So why did I use an NFA?
* Less code to write
* Eaiser to reason about
* Easier to debug (no transformations between NFA->DFA)

What would we get with a DFA?
* Linear traversal cost
* Bloated code-base (NFA->DFA transformation logic)
* Longer construction time

Fortunately, I have already written a general-purpose NFA and DFA class. Unfortunately, I will not be able to port those over until this school semester is over. For now, this is just another task on my todo list.


