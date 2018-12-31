package dijkstracalculator;

import queue.ArrayQueue;

import java.util.Stack;

public enum Operator implements OperatorInterface {


    /**
     * Enum assigning priority & execution to operators
     */


    // LOWEST priority
    ADD("+", 1){
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //System.out.println("Executing \"+\"");

            exp.setExpSolved("ADD: " + x + " " + this.getSymbol() + " " + y + " = " + (x + y));
            ansComponents.offer(exp);
            return x + y;
        }
    },
    SUBTRACT ("-", 1){
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //System.out.println("Executing \"-\"");

            exp.setExpSolved("SUBTRACT: " + x + " " + this.getSymbol() + " " + y + " = " + (x - y));
            ansComponents.offer(exp);
            return x - y;
        }
    },
    // MEDIUM priority
    MULTIPLY ("*", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //System.out.println("Executing \"*\"");

            exp.setExpSolved("MULTIPLY: " + x + " " + this.getSymbol() + " " + y + " = " + (x * y));
            ansComponents.offer(exp);
            return x * y;
        }
    },
    DIVIDE   ("/", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //System.out.println("Executing \"/\"");

            exp.setExpSolved("DIVIDE: " + x + " " + this.getSymbol() + " " + y + " = " + (x / y));
            ansComponents.offer(exp);
            return x / y;
        }
    },
    MODULUS  ("%", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {
            exp.setExpSolved("DIVIDE: " + x + " " + this.getSymbol() + " " + y + " = " + (x % y));
            ansComponents.offer(exp);
            return x % y;
        }
    },
    POWER ("^", 3) {
        @Override
        public double execute(ExpNode exp, double x, double exponent) {
            exp.setExpSolved("EXPONENT: " + x + " " + this.getSymbol() + " " + exponent + " = " + Math.pow(x, exponent));
            ansComponents.offer(exp);
            return Math.pow(x, exponent);
        }
    },
    ROOT ("root", 3) {
        @Override
        public double execute(ExpNode exp, double root, double x) {

            if(x < 0) {
                return 0;
            }

            // convert root to integer
            int intRoot = (int) root;

            switch(intRoot) {

                case 0: {
                    return 0;
                }
                case 1: {
                    return x;
                }
                case 2: { // square root

                    exp.setExpSolved("SQRT: " + this.getSymbol() + " " + x + " = " + Math.sqrt(x));
                    ansComponents.offer(exp);
                    return StrictMath.sqrt(x);
                }
                default: {

                    // @todo need to do something with negitive roots

                    double ans = StrictMath.pow(x, (1 / root));
                    double ansCeil = Math.ceil(ans);


                    if ( (root > 2 && (ansCeil - ans < .00000001)) || (root < -1 && (ansCeil - ans > .09)) ){

                        exp.setExpSolved("ROOT true: " + root + " " + this.getSymbol() + " " + x + " = " + ansCeil);
                        ansComponents.offer(exp);
                        return ansCeil;
                    } else {
                        exp.setExpSolved("ROOT false: " + root + " " + this.getSymbol() + " " + x + " = " + ans);
                        ansComponents.offer(exp);
                        return ans;
                    }
                }
            }
        }
    },
    SQRT ("sqrt", 3) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            if(x < 0) {
                return 0;
            }

            exp.setExpSolved("SQRT: " + this.getSymbol() + " " + x + " = " + StrictMath.sqrt(x));
            ansComponents.offer(exp);
            return Math.sqrt(x);
        }

        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    LOG ("log", 3)   {

        // for small roots log1p is closer to truth but using log().

        @Override
        public double execute(ExpNode expNode, double base, double exponent) {

            double result;

            result = Math.log(exponent) / Math.log(base);

            expNode.setExpSolved("LOG: " + this.getSymbol() + " base " + base + " log exp: " + exponent + " = " + result);
            ansComponents.offer(expNode);

            return result;
        }
    },
    ABS ("abs", 3) {

        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            exp.setExpSolved("ABS: " + this.getSymbol() + " " + x + " = " + Math.abs(x));
            ansComponents.offer(exp);

            return Math.abs(x);
        }

        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    DEFAULT ("def", 8) {
        @Override
        public double execute(ExpNode exp, double notUsed, double notUsedEither) {

            System.err.println(" *** Called DEFAULT.execute() ***");

            return 0;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    };

    private String symbol;
    private int priority;
    private static ArrayQueue<ExpNode> ansComponents = new ArrayQueue<>(10);

    Operator(String sym, int p) {
        symbol = sym;
        priority = p;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public abstract double execute(ExpNode exp, double x, double y);


    /**
     * if this Operator has a lower or equal priority than the operator on
     * the top of the opStack. Pop the operator on the opStack
     * and push it onto the outQueue until this operator can be
     * pushed onto the opStack. (this operator is higher than the
     * operator on the opStack)
     * @param outQueue
     * @param opStack
     */
    public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

        // int index not used.
        //System.out.println(" called Operator stackAction() " + op.getOp().getSymbol());

        // String previous, not used.
        while( ! opStack.isEmpty() && opStack.peek().getOp().getPriority() >= this.priority ) {
           outQueue.offer( opStack.pop()); // pushing the enum onto the numStack
        }
        opStack.push(op);
    }

    public static ArrayQueue<ExpNode> getAnsComponents() {
        return ansComponents;
    }

    @Override
    public boolean isUnaryOp() {
        return false;
    }

    @Override
    public String toString() {
        return this.getSymbol();
    }

    @Override
    public String getStrExpr(String x, String y, OperatorInterface op) {
        return x + " " + op + " " + y;
    }
}
