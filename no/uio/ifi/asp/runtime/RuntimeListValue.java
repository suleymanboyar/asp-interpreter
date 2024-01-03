// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;
import java.util.Iterator;

public class RuntimeListValue extends RuntimeValue {
    ArrayList<RuntimeValue> listValue = new ArrayList<>();

    public RuntimeListValue(ArrayList<RuntimeValue> v) {
        listValue = v;
    }

    @Override
    String typeName() {
        return "list";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int count = 0;
        for (RuntimeValue entry : listValue) {
            count++;
            sb.append(entry.showInfo());
            if (count < listValue.size())
                sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !listValue.isEmpty();
    }

    public ArrayList<RuntimeValue> getListValue(String what, AspSyntax where) {
        return listValue;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue))
            runtimeError("List can only b emultiplied with integer!", where);

        long v2 = v.getIntValue("index mutiplication factor", where);
        ArrayList<RuntimeValue> newList = new ArrayList<>();
        for(int i = 0; i < v2; i++){
            for(RuntimeValue runtimeValue : listValue){
                RuntimeValue dupValue = null;

                if (runtimeValue instanceof RuntimeIntegerValue)
                    dupValue = new RuntimeIntegerValue(runtimeValue.getIntValue("integer element", where));
                else if (runtimeValue instanceof RuntimeBoolValue)
                    dupValue = new RuntimeBoolValue(runtimeValue.getBoolValue("boolean element", where));
                else if (runtimeValue instanceof RuntimeStringValue)
                    dupValue = new RuntimeStringValue(runtimeValue.getStringValue("string element", where));
                else if (runtimeValue instanceof RuntimeListValue)
                    dupValue = new RuntimeListValue(runtimeValue.getListValue("list element", where));
                else if (runtimeValue instanceof RuntimeNoneValue)
                    dupValue = new RuntimeNoneValue();
                else if (runtimeValue instanceof RuntimeDictValue)
                    dupValue = new RuntimeDictValue(runtimeValue.getDictValue("dict element", where));
                else
                    runtimeError("Unreachable, found an unrecognizable element " + runtimeValue, where);

                newList.add(dupValue);
            }
        }
        return new RuntimeListValue(newList);
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue(null, null));
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntegerValue(listValue.size());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeIntegerValue))
            runtimeError("A list index must be an integer!", where);

        int v2 = (int) v.getIntValue(v.toString(), where);
        if (v2 < 0 || v2 >= listValue.size())
            runtimeError("List index " + v2 + " out of range!", where);

        RuntimeValue element = listValue.get(v2);
        if (element instanceof RuntimeIntegerValue)
            return (RuntimeIntegerValue) element;
        else if (element instanceof RuntimeBoolValue)
            return (RuntimeBoolValue) element;
        else if (element instanceof RuntimeStringValue)
            return (RuntimeStringValue) element;
        else if (element instanceof RuntimeListValue)
            return (RuntimeListValue) element;
        else if (element instanceof RuntimeNoneValue)
            return (RuntimeNoneValue) element;
        else if (element instanceof RuntimeDictValue)
            return (RuntimeDictValue) element;

        runtimeError("Found unknow list element type: " + element.typeName(), where);
        return null; // Required by the compiler!
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        int index = (int) inx.getIntValue("index", where);
        try {
            listValue.set(index, val);
        }
        catch (IndexOutOfBoundsException e) {
            runtimeError("List index " + index + "out of range!", where);
        }
    }
}
