// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.HashMap;
import java.util.Map;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<String, RuntimeValue> dictValue = new HashMap<>();

    public RuntimeDictValue(HashMap<String, RuntimeValue> dict) {
        dictValue = dict;
    }

    @Override
    String typeName() {
        return "dict";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int count = 0;
        for (Map.Entry<String, RuntimeValue> entry : dictValue.entrySet()) {
            count++;
            String key = entry.getKey();
            RuntimeValue value = entry.getValue();
            sb.append("'" + key + "': " + value.showInfo());
            if (count < dictValue.size())
                sb.append(", ");
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !dictValue.isEmpty();
    }

    @Override
    public HashMap<String, RuntimeValue> getDictValue(String what, AspSyntax where) {
        return dictValue;
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

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (!(v instanceof RuntimeStringValue))
            runtimeError("A dictionary key must be a string!", where);

        String key = v.getStringValue(v.toString(), where);
        if (!dictValue.containsKey(key)) {
            RuntimeStringValue runtimeKeyValue = new RuntimeStringValue(key);
            runtimeError("Dictionary key " + runtimeKeyValue.showInfo() + " undefined!", where);
        }

        RuntimeValue value = dictValue.get(key);
        if (value instanceof RuntimeIntegerValue)
            return (RuntimeIntegerValue) value;
        else if (value instanceof RuntimeBoolValue)
            return (RuntimeBoolValue) value;
        else if (value instanceof RuntimeStringValue)
            return (RuntimeStringValue) value;
        else if (value instanceof RuntimeListValue)
            return (RuntimeListValue) value;
        else if (value instanceof RuntimeNoneValue)
            return (RuntimeNoneValue) value;
        else if (value instanceof RuntimeDictValue)
            return (RuntimeDictValue) value;

        runtimeError("Found unknow list value type: " + value.typeName(), where);
        return null; // Required by the compiler!
    }
}
