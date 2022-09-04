program teste;
   var valores: array[0 ~ 2] of boolean;
   var x: real;
   var y: integer;

begin

    valores[0] := true;
    valores[1] := false;
    valores[2] := true;
    x := 1 / 2;
    y := 2 * 2;

    while y < 10 do y := 1 + 10;

    if y > 0 then
        begin
            x := 0;
        end
    else
        x := x + x;
end.
