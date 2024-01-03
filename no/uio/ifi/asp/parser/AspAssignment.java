package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspAssignment extends AspSmallStmt {
    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment aas = new AspAssignment(s.curLineNum());
        aas.name = AspName.parse(s);
        while (true) {
            if (s.curToken().kind != leftBracketToken) break;
            aas.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aas.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return aas;
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        for (AspSubscription subscription : subscriptions){
            subscription.prettyPrint();
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }

    //  TODO 2023-11-26: Add assignments for subscriptions
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (subscriptions.size() > 0) {
            RuntimeValue a = name.eval(curScope);
            RuntimeValue value = null;
            String id = name.toString();
            for (int i = 0; i < subscriptions.size(); i++) {
                RuntimeValue idx = subscriptions.get(i).eval(curScope);
                if (i == subscriptions.size()-1){
                    value = expr.eval(curScope);
                    a.evalAssignElem(idx, value, this);
                } else {
                    a = a.evalSubscription(idx, this);
                }
                id += "[" + idx.showInfo() + "]";
            }

            trace(id + " = " + value.showInfo());
        } else {
            String id = name.toString();
            RuntimeValue value = expr.eval(curScope);
            curScope.assign(id, value);

            trace(id + " = " + value.showInfo());
        }

        return null;
    }

}
