program { int i boolean b string s scientific sc
  // Check function declaration works,
  //   if without else
  //   relops
  //   ops
  string checkOldRelops(int x, boolean bb) {
    if(1 < 2) then {
      if(1 <=2) then {
        if(1== 2) then {
          if(1 != 2) then {
            return 42
          }
        }
      } else {
        // Ops
        return i + i - 1 / i * i
      }
    }
  }

  // Check assignment with new types
  s = "hi"
  sc = 1.23e+45

  // Check forall and function invocation
  forall int j in [ 2 .. 42 ] {
    i = write(j)
  }
}




/*program { int i int j 
   i = i + j + ( 1 + 1)
   j = write(i) + 2
}
/*