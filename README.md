# JavaFX8-Dijkstra-and-Graph-Calculator

- This calculator is an interactive calculator that uses Edgsar Dijkstra's Shunting Yard algorithm to ensure correct 
order of operations. 
- After entering the expression in the main field of the calculator, and pressing calc: 
  - The answer will show in the answer field.
  - The operations will be displayed in the order that they were calculated in a scrollpane below the answer. If the user
  hovers a mouse over the textfields, a circle will appear over the operator in the original expression. 
  - The dijkstra-parser does not expect variables such as "x" or "y". Variables should be handled by the using class. An
  example of how variables are handled are provided by the graphing calculator.
  
  Currently the parser is looking for whitespace to seperate numbers from expressions and paranthesis. Large polynomials need
  to be devided by paranthesis similar to the TI-84, the expression
  9 * 3^2 - 2 -( 3^2 + (5 + 1) / 3(5 - 3)^2
  
  should be written as to get the desired solution
  { 9 * 3 ^ 2 - 2 -{ 3 ^ 2 + ( 5 + 1 ) } } / ( 3 ( 5 - 3 ) ^ 2 )
  
  The Graphing Calculator uses the Dijkstra-Parser to calculate the graphs. Multiple graphs may be drawn drawn simultainiously
  by using a new line to seperate each graph. "y = " is expected before each polynomial. 
  
  for  example
  y = x ^ 2
  y = 2 * x ^ 2 - 3
