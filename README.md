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
  to be devided by paranthesis similar to the TI-84, the expression:
  
  9 * 3^2 - 2 -( 3^2 + (5 + 1) / 3(5 - 3)^2
  
  should be written as: 
  
  { 9 * 3 ^ 2 - 2 -{ 3 ^ 2 + ( 5 + 1 ) } } / ( 3 ( 5 - 3 ) ^ 2 )
  
  The Graphing Calculator uses the Dijkstra-Parser to calculate the graphs. Multiple graphs may be drawn drawn simultainiously
  by using a new line to seperate each graph. "y = " is expected before each polynomial. 
  
  for  example
  
  y = x ^ 2
  
  y = 2 * x ^ 2 - 3
  
  The calculator performs many operations but not all. 
  
  examples:
  
  base log exp  : 10 log 4  returns 0.602059991327962
  
  sqrt root     : sqrt 4    returns 2
  
  n root x      : 3 root 1728  returns 12
  
  absolute value: | 5 - 6 |  returns 1
  
  
  - Aditional operations can be added to the operator class. 
  1. To add an operator: Add it as an enum to the Operator class
  
  The enum is self explanatory.
  
  
  The name of the operator is in all caps. The parameters are string name, and int priority. 
  The majority of operators will be a 3. 
  
    LOG ("log", 3) {

          // for small roots log1p is closer to truth but using log().

          @Override
          public double execute(ExpNode expNode, double base, double exponent) {

              double result;
              //System.out.println(" *** Called LOG.execute() ***");

              result = Math.log(exponent) / Math.log(base);

              expNode.setExpSolved("LOG: " + this.getSymbol() + " base " + base + " log exp: " + exponent + " = " + result);
              ansComponents.offer(expNode);

              return result;
          }
      }, ...
    
      // If it is a Unary operator override the isUnaryOperator method and set it to true.
      
        @Override
          public boolean isUnaryOp() {
              return true;
          }
    
    
    
   2. Also, add the new operator to the switch in the DijkstraParser.getOperator() method

