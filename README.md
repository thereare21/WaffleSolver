# WaffleSolver
WaffleSolver is a Java program that can solve a game of Waffle. Users can enter a waffle puzzle
with their corresponding colors, and the program will return a solved waffle with every letter 
located in the correct position.

**For more information on Waffle:** https://wafflegame.net/archive

# How it Works
1. Users manually input the letters and their respective colors into the program.
2. Once the letters are in, the program runs an algorithm that solves each word one at a time.
3. For each word, the program takes all the letters that need to be changed, and iterates through all possible letters at that word and replaces them. 
4. The program then checks if the word formed from the new letters is a valid word, and generates permutations of the next word if it is.
5. A solution is found when the algorithm has found 6 valid words.



