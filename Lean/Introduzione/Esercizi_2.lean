def nTimes (action : IO Unit) : Nat → IO Unit
  | 0 => pure ()
  | n + 1 => do
    action
    nTimes action n

def countdown : Nat → List (IO Unit)
  | 0 => [IO.println "Blast off!"]
  | n + 1 => IO.println s!"{n + 1}" :: countdown n

def from5 : List (IO Unit) := countdown 5

def main : IO Unit := do
  let stdin ← IO.getStdin
  let stdout ← IO.getStdout

  stdout.putStrLn "How would you like to be addressed?"
  let input ← stdin.getLine
  let name := input.dropRightWhile Char.isWhitespace

  stdout.putStrLn s!"Hello, {name.dropWhile Char.isWhitespace}!"

  nTimes (IO.println "SUCA") 10
