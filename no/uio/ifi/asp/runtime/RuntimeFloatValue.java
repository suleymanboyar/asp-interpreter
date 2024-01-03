package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double v) {
        floatValue = v;
    }

    @Override
    String typeName() {
        return "float";
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            return (long) floatValue;
        } catch (NumberFormatException e){
            return  super.getIntValue(typeName(), where);
        }
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return floatValue != 0.0;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return this;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for +.", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeFloatValue(floatValue + v2);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for -", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeFloatValue(floatValue - v2);
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for *", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeFloatValue(floatValue * v2);
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for /", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeFloatValue(floatValue / v2);
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for *", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);;
        return new RuntimeFloatValue(Math.floor(floatValue / v2));
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for *", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeFloatValue(floatValue - v2 * Math.floor(floatValue / v2));
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        else if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for ==", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue == v2);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        else if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)) {
            runtimeError("Type error for !=", where);
        }

        double v2  = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue != v2);
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for >", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue > v2);
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for >=", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue >= v2);
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for <", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue < v2);
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue) && !(v instanceof RuntimeFloatValue)){
            runtimeError("Type error for <=", where);
        }

        double v2 = v.getFloatValue(v.toString(), where);
        return new RuntimeBoolValue(floatValue <= v2);
    }


    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(null, null));
    }

}
