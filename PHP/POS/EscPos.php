<?php

// BISOGNA VERIFICARE CON https://www.epson-biz.com/modules/ref_escpos/index.php
// PERCHE' IL PDF CONTIENE ERRORI ED IMPRECISIONI

require_once "EscPosPrinter.class.php";
require_once "EscPosParser.class.php";

$fn=@$argv[1];
if (!!$fn) {
  @unlink($fn.".png");
  $stampante = new EscPosPrinter();
  $scontrino = new EscPosParser(file_get_contents($fn));
  $scontrino->parse($stampante);
  $stampante->export($fn.".png");
} else {
  die("errore , manca il file");
}
