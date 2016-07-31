# dictionary-store


While solving a word puzzle, i generate words with all combinations of the alphabets(a-z) 
and filter them if it is a dictionary word. So i want a data-structure
to store english dictionary words and then perform a look-up to check if it is a dictionary word.
There are diffirent options to store word-list as Strings for easy look-up like Hashmap, Trie.

While searching I found [other option](http://stackoverflow.com/questions/2276641/way-to-store-a-large-dictionary-with-low-memory-footprint-fast-lookups-on-and) converting the word-list to Long's then searching using binary search. It consumes less memory when copared to other options.
 
 
This should work better than search trees as the dictionary is constant and binary search should be enough. Search trees sould be used when we need to modify the data.