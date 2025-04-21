LoadPackage("NumericalSgps");

controlla := function(n1, n2, formula)
    for i in [n2+1..1000] do
        if (Gcd(n1,n2,i) = 1) then
            ss := NumericalSemigroup([n1,n2,i]);
            if not (FrobeniusNumber(ss) = formula + i) then
                Print(i);
                Print("\n");
                break;
            fi;
        fi;
    od;
end;

n1 := 6;
n2 := 10;
controlla(n1, n2, n1 + n2 - 2);