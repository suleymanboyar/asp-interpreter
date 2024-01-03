package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeIntegerValue extends RuntimeValue {
    long intValue;

    public RuntimeIntegerValue(long v) {
        intValue = v;
    }

    @Override
    String typeName() {
        return "int";
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return intValue != 0;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) intValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            RuntimeIntegerValue vInt = (RuntimeIntegerValue) v;
            return new RuntimeIntegerValue(intValue + vInt.intValue);
        }
        else if (v instanceof RuntimeFloatValue) {
            RuntimeFloatValue vFloat = (RuntimeFloatValue) v;
            return new RuntimeFloatValue(intValue + vFloat.floatValue);
        }

        runtimeError("Type error for +.", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for /", where);
        } else if (v.getFloatValue(v.toString(), where) == 0.0) {
            runtimeError("Zero devision error", where);
        }
        return new RuntimeFloatValue(intValue / v.getFloatValue(v.toString(), where));
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue == y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue == y);
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue > y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue > y);
        }
        runtimeError("Type error for >", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue >= y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue >= y);
        }
        runtimeError("Type error for >=", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeIntegerValue(Math.floorDiv(intValue, y));
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeFloatValue(Math.floor(intValue / y));
        }
        runtimeError("Type error for //", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue < y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue < y);
        }
        runtimeError("Type error for <", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
            if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue <= y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue <= y);
        }
        runtimeError("Type error for <=", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeIntegerValue(Math.floorMod(intValue, y));
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeFloatValue(intValue - y * Math.floor(intValue / y));
        }
        runtimeError("Type error for %", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeIntegerValue(intValue * y);
        } else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeFloatValue(intValue * y);
        }
        runtimeError("Type error for *", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntegerValue(-intValue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(null, null));
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue) {
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeBoolValue(intValue != y);
        }
        else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeBoolValue(intValue != y);
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=", where);
        return null; // Required by the compiler!
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return this;
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntegerValue){
            long y = v.getIntValue(v.toString(), where);
            return new RuntimeIntegerValue(intValue - y);
        }
        else if (v instanceof RuntimeFloatValue) {
            double y = v.getFloatValue(v.toString(), where);
            return new RuntimeFloatValue(intValue - y);
        }

        runtimeError("Type error for -", where);
        return null;
    }
}
