package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    ArrayList<AspPrimarySuffix> primariesSuffixes = new ArrayList<>();
    AspAtom atom;

    AspPrimary(int n) {
        super(n);
    }

    public static AspPrimary parse(Scanner s) {
        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum());
        ap.atom = AspAtom.parse(s);

        while (true) {
            if (!s.isPrimarySuffix()) break;
            ap.primariesSuffixes.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    public void prettyPrint() {
        atom.prettyPrint();
        for (AspPrimarySuffix aps : primariesSuffixes)
            aps.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = atom.eval(curScope);
        for (AspPrimarySuffix suffix : primariesSuffixes) {
            if (suffix instanceof AspSubscription) {
                v = v.evalSubscription(suffix.eval(curScope), this);
            } else if (suffix instanceof AspArguments) {
                RuntimeListValue actualParams = (RuntimeListValue) suffix.eval(curScope);
                String funcName = v.toString();
                String arguments = actualParams.toString();
                trace("Call function " + funcName + " with params " + arguments);

                v = v.evalFuncCall(actualParams.getListValue("arguments", this), this);
                // if (v instanceof RuntimeNoneValue)
                //     trace("Expression statement produced None");
            } else {
                assert false : "Unreachable, AspPrimary.eval() only supports AspArguments and AspSubscription, and not: "
                    + suffix.getClass().getSimpleName();
            }
        }
        return v;
    }
}
