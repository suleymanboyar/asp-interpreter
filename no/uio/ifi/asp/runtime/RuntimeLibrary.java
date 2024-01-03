// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // Hentet fra IN2030 2023 forelesning
        assign("len", new RuntimeFunc("len") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 1, "len", where);
                    return actualParams.get(0).evalLen(where);
                }});

        // Hentet fra IN2030 2023 forelesning
        assign("print", new RuntimeFunc("print") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    for (int i = 0; i < actualParams.size(); ++i) {
                        if (i > 0) System.out.print(" ");
                        System.out.print(actualParams.get(i).toString());
                    }
                    System.out.println();
                    return new RuntimeNoneValue();
                }});

        assign("float", new RuntimeFunc("float") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 1, "float", where);
                    RuntimeValue param = actualParams.get(0);
                    double f = param.getFloatValue(param.typeName(), where);
                    return new RuntimeFloatValue(f);
                }});

        assign("int", new RuntimeFunc("int") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 1, "int", where);
                    RuntimeValue param = actualParams.get(0);
                    long n = param.getIntValue(param.typeName(), where);
                    return new RuntimeIntegerValue(n);
                }});

        assign("str", new RuntimeFunc("str") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 1, "str", where);
                    String s = actualParams.get(0).toString();
                    return new RuntimeStringValue(s);
                }});

        assign("range", new RuntimeFunc("range") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 2, "range", where);
                    long v1 = actualParams.get(0).getIntValue(actualParams.get(0).typeName(), where);
                    long v2 = actualParams.get(1).getIntValue(actualParams.get(1).typeName(), where);
                    ArrayList<RuntimeValue> range = new ArrayList<>();
                    for (long i = v1; i < v2; i++) {
                        range.add(new RuntimeIntegerValue(i));
                    }
                    return new RuntimeListValue(range);
                }});

        assign("input", new RuntimeFunc("input") {
                @Override
                public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                    checkNumParams(actualParams, 1, "input", where);
                    String output = actualParams.get(0).getStringValue(actualParams.get(0).typeName(), where);
                    Scanner in = new Scanner(System.in);
                    System.out.print(output);
                    String input = null;
                    try {
                        input = in.nextLine();
                    } catch (NoSuchElementException e){
                        runtimeError("No more lines to read", where);
                    }

                    return new RuntimeStringValue(input);
                }});
    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}

