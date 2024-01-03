package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;


public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(String name, RuntimeScope scope, AspFuncDef def) {
        this.name = name;
        this.defScope = scope;
        this.def = def;
    }

    public RuntimeFunc(String name) {
        this.name = name;
    }

    @Override
    String typeName() {
        return "func";
    }

    @Override
    public String toString() {
        return name;
    }

    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        if (actualParams.size() != def.getArgs().size()) {
            runtimeError("Wrong number of parameters calling " + name + "!", where);
        }

        RuntimeScope newScope = new RuntimeScope(defScope);

        // Assign the actual arguments to the formal parameters
        for (int i = 0; i < actualParams.size(); i++) {
            RuntimeValue val = actualParams.get(i);
            String id = def.getArgNameAt(i);
            newScope.assign(id, val);
        }

        try {
            def.evalBody(newScope);
        } catch (RuntimeReturnValue rrv){
            return rrv.value;
        }

        return new RuntimeNoneValue(); // Required by the compiler!
    }

}
