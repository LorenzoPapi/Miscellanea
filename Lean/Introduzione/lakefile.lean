import Lake
open Lake DSL

package «Introduzione» where
  -- add package configuration options here

lean_lib «Introduzione» where
  -- add library configuration options here

@[default_target]
lean_exe «introduzione» where
  root := `Main
