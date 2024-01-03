// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String stringValue;

    public RuntimeStringValue(String v) {
        stringValue = v;
    }

    @Override
    String typeName() {
        return "string";
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public String showInfo() {
        if (stringValue.indexOf("'") >= 0)
            return '"' + stringValue + '"';
        else
            return "'" + stringValue + "'";
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return stringValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException e){
            return  super.getIntValue(typeName(), where);
        }
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        try {
            return Double.parseDouble(stringValue);
        } catch (NumberFormatException e){
            return  super.getFloatValue(typeName(), where);
        }
    }


    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !stringValue.equals("");
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        String v2 = v.getStringValue(v.toString(), where);
        return new RuntimeStringValue(stringValue + v2);
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        StringBuilder sb = new StringBuilder();
        if (!(v instanceof RuntimeIntegerValue)) {
            return super.evalMultiply(v, where);
        }

        long v2 =  v.getIntValue(v.toString(), where);
        for (int i = 0; i < v2; i++)
            sb.append(stringValue);

        return new RuntimeStringValue(sb.toString());
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue)
            return new RuntimeBoolValue(false);

        String v2 = v.getStringValue(v.toString(), where);
        // Maybe use .compareTo() since it does not accept null
        return new RuntimeBoolValue(stringValue.equals(v2));
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue)
            return new RuntimeBoolValue(true);

        String v2 = v.getStringValue(v.toString(), where);
        // Maybe use .compareTo() since it does not accept null
        return new RuntimeBoolValue(!stringValue.equals(v2));
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        String v2 = v.getStringValue(v.toString(), where);
        return new RuntimeBoolValue(stringValue.compareTo(v2) < 0);
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        String v2 = v.getStringValue(v.toString(), where);
        return new RuntimeBoolValue(stringValue.compareTo(v2) <= 0);
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        String v2 = v.getStringValue(v.toString(), where);
        return new RuntimeBoolValue(stringValue.compareTo(v2) > 0);
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        String v2 = v.getStringValue(v.toString(), where);
        return new RuntimeBoolValue(stringValue.compareTo(v2) >= 0);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(null, null));
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntegerValue(stringValue.length());
    }

    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue))
            runtimeError("A string index must be an integer!", where);

        int v2 = (int) v.getIntValue(v.toString(), where);
        if (v2 < 0 || v2 >= stringValue.length() )
            runtimeError("String index " + v2 + " out of range!", where);

        return new RuntimeStringValue(stringValue.substring(v2, v2 + 1));
    }
}
