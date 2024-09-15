/-
#eval <expr> => result
#check <expr> => type of expr
def var : type := <content>
def fun (arg1 : type1) (arg2 : type2) ... : returnType :=
  <function body>
  <return value>
abbrev ab : type := var
structure name where
  param1 : type1
  param2 : type2
deriving Repr

-/

/-

Evaluating expressions, Types

-/

#eval 42+19
#eval String.append "A" (String.append "B" "C")
#eval String.append (String.append "A" "B") "C"
#eval if 3 == 3 then 5 else 7
#eval if 3 == 4 then "equal" else "not equal"
#check (1-2 : Int)

/-

Functions and definitions

-/

def hello : String := "Hello"
#eval String.append hello " world!"

def add1 (n : Nat) : Nat := n+1
#eval add1 8

def maximum (m : Nat) (n : Nat) : Nat :=
  if m > n then m else n

#eval maximum (5*8) (7*2)

def joinStringWith (sep : String) (first : String) (second : String) : String :=
  String.append first (String.append sep second)

#eval joinStringWith "," "ciao" " ciao"
#check(joinStringWith)

def volume_abc(a : Nat) (b: Nat) (c: Nat) : Nat := a*b*c
#eval volume_abc 3 5 6

def Str : Type := String
def yes : Str := "yes"

abbrev N: Type := Nat
def thirthynine : N := 39

/-

Structures

-/

structure Point where
  point ::
  x: Float
  y: Float
deriving Repr

def origin : Point := {x := 0.0, y:= 0.0}
#eval origin

def addPoints (p1: Point) (p2 : Point) : Point :=
  { x := p1.x + p2.x, y := p1.y + p2.y }

#eval addPoints {x := 2.0, y:= 3.0} {x := 7.0, y:= -9.0}

def distance (p1 : Point) (p2 : Point) : Float :=
  Float.sqrt (((p2.x - p1.x) ^ 2.0) + ((p2.y - p1.y) ^ 2.0))

#eval distance { x := 1.0, y := 2.0 } { x := 5.0, y := -1.0 }

structure Point3D where
  x : Float
  y : Float
  z : Float
deriving Repr

def origin3D : Point3D := { x := 0.0, y := 0.0, z := 0.0 }

def zeroX (p : Point) : Point :=
  { p with x := 0 }

def pointy : Point := {x := 5.3, y:= 203}
#eval pointy
#eval zeroX pointy
#eval pointy
#check (Point.point)

structure RectangularPrism where
  h: Float
  w: Float
  d: Float
deriving Repr

def volume_prism (p: RectangularPrism) : Float :=
  p.h * p.w * p.d

structure Segment where
  a: Point
  b: Point
deriving Repr

def length_segment (s: Segment) : Float :=
  Float.sqrt ((s.a.x - s.b.x)^2 + (s.a.y - s.b.y)^2)

/-

Datatype, Patterns, Recursion

-/

def isZero (n : Nat) : Bool :=
  match n with
  | Nat.zero => true
  | Nat.succ _ => false

def pred (n : Nat) : Nat :=
  match n with
  | Nat.zero => Nat.zero
  | Nat.succ k => k

def depth (p: Point3D) : Float :=
  match p with
  | {x:= _, y:= _, z:= d} => d

def even (n : Nat) : Bool :=
  match n with
  | Nat.zero => true
  | Nat.succ k => not (even k)

def plus (n: Nat) (k: Nat) : Nat :=
  match k with
  | Nat.zero => n
  | Nat.succ k' => Nat.succ (plus n k')

def times (n : Nat) (k : Nat) : Nat :=
  match k with
  | Nat.zero => Nat.zero
  | Nat.succ k' => plus n (times n k')

def minus (n : Nat) (k : Nat) : Nat :=
  match k with
  | Nat.zero => n
  | Nat.succ k' => pred (minus n k')

/-

Polymorphism

-/

structure PPoint (α : Type) where
  x: α
  y: α
deriving Repr

def natOrigin : PPoint Nat := {x:= 0, y:= 0}

def replaceX (α : Type) (pp : PPoint α) (newx: α) :=
  {pp with x:= newx}
#eval replaceX Nat natOrigin 20

inductive Sign where
  | pos
  | neg

def posOrNegThree (s : Sign) : match s with | Sign.pos => Nat | Sign.neg => Int :=
  match s with
  | Sign.pos => (3: Nat)
  | Sign.neg => (-3: Int)

def primesUnder10 : List Nat := [2,3,5,7]

def replaceX_inferred {α : Type} (pp : PPoint α) (newx: α) :=
  {pp with x:= newx}
#eval replaceX_inferred natOrigin 20

#eval primesUnder10.head?

def fives : String × Int := { fst:="five", snd:= 5 }
def fives_abbr : String × Int := ("fives", 5)

def PetName_old :Type := String ⊕ String
def animals : List PetName_old :=
  [Sum.inl "Spot", Sum.inr "Tiger", Sum.inl "Fifi", Sum.inl "Rex", Sum.inr "Floof"]
def howManyDogs (pets : List PetName_old) : Nat :=
  match pets with
  | [] => 0
  | Sum.inl _ :: morePets => howManyDogs morePets + 1
  | Sum.inr _ :: morePets => howManyDogs morePets
#eval howManyDogs animals

def List.last {α : Type} (l : List α) : Option α :=
  match l with
  | [] => Option.none
  | List.cons _ [] => l.head?
  | List.cons _ t => t.last

#eval [8,2,3].last
#eval primesUnder10.last

def List.findFirst? {α : Type} (xs : List α) (predicate : α → Bool) : Option α :=
  match xs with
  | [] => Option.none
  | List.cons h t => if predicate h then h else (t.findFirst? predicate)

#eval [0].findFirst? (· < 3)

def Prod.swap {α β : Type} (pair : α × β) : β × α :=
  (pair.snd, pair.fst)

#eval {fst:="19",snd:=8 : String×Nat}.swap

def zip {α β : Type} (xs : List α) (ys : List β) : List (α × β) :=
  match xs, ys with
  | a::arest, b::brest => (a,b) :: zip arest brest
  | _, _ => []

#eval zip [1,3,4] [5,6,7,8]

def take {α : Type} (num : Nat) (l : List α) : List α :=
  if num >= l.length then l else match l with
  | a::arest => if num > 0 then a :: take (num-1) arest else []
  | []=>[]

#eval take 3 ["bolete", "oyster"]
#eval take 1 ["bolete", "oyster"]
