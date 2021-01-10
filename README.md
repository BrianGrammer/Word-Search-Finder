# Word-Search-Finder
This Program Finds a Word within a given text file using backtracking.\
An explanation of the algorithm is given below.\
Pass in row, column, word, location (current letter in the word), board\
Checks right, then down, then left, then up\
1st Checks boundary\
2nd Check if letters match\
	-if it matches \
		1) add letter to current word\
		2) “make letter uppercase” to communicate that we already checked it\
		3) checks the base case: if loc = word.length()-1;\
		4) recurse – go to order of operations
Order of operations\
1)	Right (c+1)\
2)	Down (r + 1)\
3)	Left (c-1)\
4)	Up (r-1)\
5)	Backtrack 
