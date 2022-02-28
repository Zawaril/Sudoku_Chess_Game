
Backtracking Algorithm Summary

The idea is to keep placing valid numbers in the solution cells 
ascendingly (1 to 9). Kepe doing this unitl a random solution cell appears 
in which no number is valid. This triggers backtracking. The algo goes back to 
the previous solution cell, and updates whatever number was placed to a greater 
number.
If a greater number is found. go to the next cell and keep placing a valid 
number starting from 1 to 9.
If no valid number is found, backtrack again, and repeat the steps.

Visualizing this will be far easier so here is a small video to help you out
if needed.

Tech With Tim
https://www.youtube.com/watch?v=eqUwSA0xI-s&t=509s


HOW TO USE

. provide the text file directory of the puzzle you want to solve in FileInputStream()
. create a ChessSudoku object
. apply any one of the chess rules if you want to by turning the boolean value 'true'
  eg: s.kingRule = true
. s.solve(false) solves the puzzle
. s.print() prints the grid

OTHERS

. This version does not find all solutions of a puzzle therefore the boolean
parameter in solve(boolean_param) always remains 'false'

. if a puzzle has no solution, it throws an exception which prints "No Solution"

. If the queen rule only applies for the number 9 for any SIZE x SIZE puzzle, then 
make this change:
(line 112) --> if (queenRule && num == 9)
However, if the rule applies for the largest number in any SIZE x SIZE puzzle, 
then leave it be
eg:- SIZE 3 then 9, SIZE 4 then 16, SIZE 5 then 25 etc.




UPDATE FROM PREVIOUS VERSION


This version (v2) has the capability of finding all solutions. However, for very hard 4x4 or 5x5 puzzles 
it will take a long time to run the code. THe algorithm will fins the orrect solutions, but 
it will take a long time. You should try it yourself.

The modification is simply after finsing a solution, backtrack once so that the algo tries
to find another correct solution.


VERY IMPORTANT THINGS TO NOTE

A - PRINTING ALL SOLUTIONS

There is private method in the class called printSolutions(). It prints all solutions of the 
puzzle. It is commented in line 649, 650. This is just for visual purposes. 
You can remove it any time.



B - EXCEPTIONS

In the previous algorithm (v1), there is a try catch error block which prints "No Solution".
This happens if there is no solution to the given puzzle.

PLEASE UNDERSTAND as the try-catch block is PRINTING "No Solution", check with your faculty if 
it is acceptable to print it. If it should not be printed, remove/comment JUST the 
print line - line 172 (for v1)

For this algorithm i.e. v2

#1 - if parameter of solve = false and no solution is found
 try-catch block, just like v1, prints "No Solution"

#2 - if parameter of solve = true and no solution is found
 try-catch block, just like v1, prints "No Solution"

#2 - if parameter of solve = true and one or more solutions are found
 try-catch block prints "All solutions have been added to solutions set"

AGAIN, if these print statements are not to be printed comment/remove them - lines 218, 221, 224



C - s.print()

The print method prints the grid matrix, in this algo the grid matrix resorts back to the text file
puzzle sequence after finsing all the solutions. 
All the solutions have been added to the solutions HashSet.



