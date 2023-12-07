<?php

class EscPosParser{
	protected $printer=null;
	protected $contents=[];
	protected $ptr=0;

	public function __construct($contents) {
		$this->contents=array_values(unpack('C*', $contents));
		$this->ptr=0;
	}

	private function dump($d) {
		return json_encode($this->hexdump($d));
	}

	private function hexdump($d) {
		$h=[];
		foreach($d as $v) {
			$h[]=(is_array($v)?$this->hexdump($v):sprintf("%02X",$v));
		}
		return $h;
	}

	private function error() {
		die(sprintf("Called by %s\nError parsing at position 0x%04X\n%02X %02X %02X %02X\n", debug_backtrace(!DEBUG_BACKTRACE_PROVIDE_OBJECT|DEBUG_BACKTRACE_IGNORE_ARGS,2)[0]['line'], $this->prev(), $this->contents[$this->ptr], $this->contents[$this->ptr+1], $this->contents[$this->ptr+2], $this->contents[$this->ptr+3]));
	}

	private function eof() {
		return !($this->ptr < count($this->contents));
	}

	private function next() {
		return $this->contents[$this->ptr++];
	}

	private function prev() {
		return $this->ptr--;
	}

	private function getNullTerminatedArray() {
		$ret=[];
		while(true){
			if(!$this->eof()) {
				$ch=$this->next();
				$ret[]=$ch;
				if($ch==0) break;
			} else {
				$this->error();
			}
		}
		return $ret;
	}
	
	private function getString() {
		$ret='';
		while (!$this->eof()) {
			$ch=$this->next();
			//if (($ch >= 32) and ($ch <= 126)) {
			// bisogna implementare le codepage attivate da ESC t
			if (($ch >= 32)) {
				$ret.=chr($ch);
			} else {
				$this->prev();
				break;
			}
		}
		return $ret;
	}

	private function getByte() {
		$ret=false;
		if(!$this->eof()) {
			$ret=$this->next();
		} else {
			$this->error();
		}
		return $ret;
	}

	/*private function getWord() {
		$ret=false;
		if(!$this->eof()) {
			$ret=$this->next();
			if(!$this->eof()) {
				$ret|=($this->next() << 8);
			} else {
				$this->prev();
				$ret=false;
				$this->error();
			}
		} else {
			$this->error();
		}
		return $ret;
	}*/

	private function getWord() {
		$ret=0;
		$i=0;
		while(!$this->eof() && $i<16) {
			$ret|=($this->next() << $i);
			$i += 8;
		}
		if($this->eof()) {
			$ret=false;
			$this->error();
		}
		return $ret;
	}

	private function getDWord() {
		$ret=0;
		$i=0;
		while(!$this->eof() && $i<32) {
			$ret|=($this->next() << $i);
			$i += 8;
		}
		if($this->eof()) {
			$ret=false;
			$this->error();
		}
		return $ret;
	}

	public function parse($printer=null) {
		if (!!$printer) $this->printer=$printer;
		if (!$this->printer) $this->error();
		while(!$this->eof()) {
			$code = $this->getString();
			if (!!$code) $this->printer->doString($code);
			$code = $this->getByte();
			switch ($code) {
				case 0x09: $this->printer->doHT();     break;
				case 0x0A: $this->printer->doLF();     break;
				case 0x0D: $this->printer->doCR();     break;
				case 0x0C: $this->printer->doFF();     break;
				case 0x18: $this->printer->doCAN();    break;
				case 0x10: $this->parseDLE(); break;
				case 0x1B: $this->parseESC(); break;
				case 0x1C: $this->parseFS();  break;
				case 0x1D: $this->parseGS();  break;
				default: $this->error();
			}
		}
		print("\n ! !   T H E   E N D   ! ! \n");
	}

	private function parseDLE() {
		$code = $this->getByte();
		switch ($code) {
				case 0x04: $this->parseDLE_EOT(); break;
				case 0x05: $this->parseDLE_ENQ(); break;
				case 0x14: $this->parseDLE_DC4(); break;
			default: $this->error();
		}
	}

	private function parseESC() {
		$code = $this->getByte();
		switch ($code) {
				case 0x0c: $this->printer->doESC_0C(); break;
				case 0x20: $this->printer->doESC_20($this->getByte()); break;
				case 0x21: $this->printer->doESC_21($this->getByte()); break;
				case 0x24: $this->printer->doESC_24($this->getWord()); break;
				case 0x25: $this->printer->doESC_25($this->getByte()); break;
				case 0x26:
						$y = $this->getByte();
						if (!in_array($y,[2,3])) $this->error();
						$c1 = $this->getByte();
						$c2 = $this->getByte();
						if (($c1<0x20)||($c2>0x7E)||($c1>$c2)) $this->error();
						for ($c=$c1;$c<=$c2;$c++) {
							$x[$c] = $this->getByte();
							if ($x[$c]>12) $this->error();
							$d[$c]=[];
							for($yy=0;$yy<$y;$yy++) {
								for($xx=0;$xx<$x[$c];$xx++) {
									$d[$c][$yy][$xx] = $this->getByte();
								}
							}
						}
						$this->printer->doESC_26($y, $c1, $c2, $x, $d);
					break;
				case 0x28:
					$f = $this->getByte();
					$p = $this->getWord();
					for($i=0;$i<$p;$i++) $d[]=$this->getByte();
					$this->printer->doESC_28($f, $p, $d);
					break;
				case 0x2A: 
						$m = $this->getByte();
						if (!in_array($m,[0,1,0x20,0x21])) $this->error();
						$y = (!!($m & 0x20)?3:1);
						$n = $this->getWord();
						if ($n>0x960) $this->error();
						$d=[];
						for($xx=0;$xx<$n;$xx++) {
							for($yy=0;$yy<$y;$yy++) {
								$d[$xx][$yy] = $this->getByte();
							}  
						}
						$this->printer->doESC_2A($m, $n, $d);
					break;
				case 0x2D: 
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,0x30,0x31,0x32])) $this->error();
						$this->printer->doESC_2D($n);
					break;
				case 0x32: $this->printer->doESC_32(); break;
				case 0x33: $this->printer->doESC_33($this->getByte()); break;
				case 0x3C: $this->printer->doESC_3C(); break;
				case 0x3D: $this->printer->doESC_3D($this->getByte()); break;
				case 0x3F:
						$n = $this->getByte();
						if (($n<0x20)||($n>0x7E)) $this->error();
						$this->printer->doESC_3F($n);
					break;
				case 0x40: $this->printer->doESC_40(); break;
				case 0x44: $this->printer->doESC_44($this->getNullTerminatedArray()); break;
				case 0x45: $this->printer->doESC_45($this->getByte()); break;
				case 0x47: $this->printer->doESC_47($this->getByte()); break;
				case 0x4A: $this->printer->doESC_4A($this->getByte()); break;
				case 0x4B:
					$n = $this->getByte();
					if ($n>0x30) $this->error();
					$this->printer->doESC_4B($n);
					break;
				case 0x4C: $this->printer->doESC_4C(); break;
				case 0x4D: 
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,3,4,0x40,0x41,0x42,0x43,0x44,0x61,0x62])) $this->error();
						$this->printer->doESC_4D($n);
					break;
				case 0x52: $this->printer->doESC_52($this->getByte()); break;
				case 0x53: $this->printer->doESC_53(); break;
				case 0x54:
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,3,0x40,0x41,0x42,0x43])) $this->error();
						$this->printer->doESC_54($n);
					break;
				case 0x55: $this->printer->doESC_55($this->getByte()); break;
				case 0x56:
					$n = $this->getByte();
					if (!in_array($n,[0,1,0x40,0x41])) $this->error();
					$this->printer->doESC_56($n);
					break;
				case 0x57: $this->printer->doESC_57($this->getWord(),$this->getWord(),$this->getWord(),$this->getWord()); break;
				case 0x5C: $this->printer->doESC_5C($this->getWord()); break;
				case 0x61:
					$n = $this->getByte();
					if (!in_array($n,[0,1,2,0x40,0x41,0x42])) $this->error();
					$this->printer->doESC_61($n);
					break;
				case 0x63:
					$y = $this->getByte();
					if (!in_array($y,[3,4,5])) $this->error();
					$this->printer->doESC_63($y, $this->getByte());
					break;
				case 0x64: $this->printer->doESC_64($this->getByte()); break;
				case 0x65: $this->printer->doESC_65($this->getByte()); break;
				case 0x69: $this->printer->doESC_69(); break;
				case 0x6D: $this->printer->doESC_6D(); break;
				case 0x70:
						$m = $this->getByte();
						if (!in_array($m,[0,1,0x30,0x31])) $this->error();
						$this->printer->doESC_70($m,$this->getByte(),$this->getByte());
					break;
				case 0x72:
						$n = $this->getByte();
						if (!in_array($n,[0,1,0x30,0x31])) $this->error();
						$this->printer->doESC_72($n);
					break;
				case 0x74:
						$n = $this->getByte();
						if (($n>82)&&($n<254)) $this->error();
						$this->printer->doESC_74($n);
					break;
				case 0x75:
						$n = $this->getByte();
						if (!in_array($n,[0,0x40])) $this->error();
						$this->printer->doESC_75($n);
					break;
				case 0x76: $this->printer->doESC_76(); break;
				case 0x7B: $this->printer->doESC_7B($this->getByte()); break;
			default: $this->error();
		}
	}

	private function parseFS() {
		$code = $this->getByte();
		switch ($code) {
				case 0x21: $this->printer->doFS_21($this->getByte()); break;
				case 0x26: $this->printer->doFS_26(); break;
				case 0x28:
					$f = $this->getByte();
					$p = $this->getWord();
					for($i=0;$i<$p;$i++) $d[]=$this->getByte();
					$this->printer->doFS_28($f, $p, $d);
					break;
				case 0x2D: $this->printer->doFS_2D($this->getByte()); break;
				case 0x2E: $this->printer->doFS_2E(); break;
				case 0x32: die("\nStop."); break;
				case 0x3F: $this->printer->doFS_3F($this->getByte(),$this->getByte()); break;
				case 0x43:
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,0x30,0x31,0x32])) $this->error();
						$this->printer->doFS_43($n);
					break;
				case 0x53: $this->printer->doFS_53($this->getByte(),$this->getByte()); break;
				case 0x57: $this->printer->doFS_57($this->getByte()); break;
				case 0x67:
					$f = $this->getByte();
					$m = $this->getByte();
					$a = $this->getDWord();
					$n = $this->getWord();
					$d = [];
					if ($f == 0x32) for($i=0;$i<$n;$i++) $d[]=$this->getByte();
					$this->printer->doFS_67($f,$m,$a,$n,$d);
					break;
				case 0x70:
						$n = $this->getByte();
						$m = $this->getByte();
						if (!in_array($n,[0,1,2,3,0x30,0x31,0x32,0x33])) $this->error();
						$this->printer->doFS_70($n, $m);
					break;
				case 0x71:
						$n = $this->getByte();
						$x=[]; $y=[]; $d=[];
						for($i=0;$i<$n;$i++) {
							$x[$i] = $this->getWord();
							if ($x[$i]>1023) $this->error();
							$y[$i] = $this->getWord();
							if ($y[$i]>1023) $this->error();
							for($yy=0;$yy<$y[$i];$yy++) {
								for($xx=0;$xx<$x[$i];$xx++) {
									$d[$i][$yy][$xx] = $this->getByte();
								}
							}
						}
						$this->printer->doFS_71($n, $x, $y, $d);
					break;
			default: $this->error();
		}
	}

	private function parseGS() {
		$code = $this->getByte();
		switch ($code) {
				case 0x21: $this->printer->doGS_21($this->getByte()); break;
				case 0x24: $this->printer->doGS_24($this->getWord()); break;
				case 0x28:
						$f = $this->getByte();
						$p = $this->getWord();
						for($i=0;$i<$p;$i++) $d[]=$this->getByte();
						$this->printer->doGS_28($f, $p, $d);
					break;
				case 0x2A:
						$x = $this->getByte();
						$y = $this->getByte();
						if (($x<1)||($y<1)) $this->error();
						$d=[];
						for($yy=0;$yy<$y;$yy++) {
							for($xx=0;$xx<8*$x;$xx++) {
								$d[$yy][$xx] = $this->getByte();
							}
						}
						$this->printer->doGS_2A($x, $y, $d);
					break;
				case 0x2F:
						$m = $this->getByte();
						if (!in_array($m,[0,1,2,3,0x30,0x31,0x32,0x33])) $this->error();
						$this->printer->doGS_2F($m);
					break;
				case 0x3A: $this->printer->doGS_3A(); break;
				case 0x38:
						$f = $this->getByte();
						$p = $this->getWord();
						for($i=0;$i<$p;$i++) $d[]=$this->getByte();
						$this->printer->doGS_28($f, $p, $d);
					break;
				case 0x42: $this->printer->doGS_42($this->getByte()); break;
				case 0x43:
						$f = $this->getByte();
						switch ($f) {
							case 0x30:
									$n = $this->getByte();
									if ($n>5) $this->error();
									$m = $this->getByte();
									if (!in_array($m,[0,1,2,0x30,0x31,0x32]));
									$this->printer->doGS_43_30($n,$m);
								break;
							case 0x31: $this->printer->doGS_43_31($this->getWord(),$this->getWord(),$this->getByte(),$this->getByte()); break;
							case 0x32: $this->printer->doGS_43_32($this->getWord()); break;
							case 0x3B:
									$sa = $this->getWord(); if(!in_array($this->getByte(),[0x3B])) $this->error();
									$sb = $this->getWord(); if(!in_array($this->getByte(),[0x3B])) $this->error();
									$sn = $this->getByte(); if(!in_array($this->getByte(),[0x3B])) $this->error();
									$sr = $this->getByte(); if(!in_array($this->getByte(),[0x3B])) $this->error();
									$sc = $this->getWord(); if(!in_array($this->getByte(),[0x3B])) $this->error();
									$this->printer->doGS_43_3B($sa,$sb,$sn,$sr,$sc);
								break;
							default: $this->error(); break;
						}
					break;
				case 0x44: die("\nStop."); break;
				case 0x48:
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,3,0x40,0x41,0x42,0x43])) $this->error();
						$this->printer->doGS_48($n);
					break;
				case 0x49: $this->printer->doGS_49($this->getByte()); break;
				case 0x4C: $this->printer->doGS_4C($this->getWord()); break;
				case 0x50: $this->printer->doGS_50($this->getByte(),$this->getByte()); break;
				case 0x51:
						if (!in_array($this->getByte(),[0x30])) $this->error();
						$m = $this->getByte();
						if (!in_array($m,[0,1,2,3,0x40,0x41,0x42,0x43])) $this->error();
						$x = $this->getWord();
						if ($x<1 || $x>4256) $this->error();
						$y = $this->getWord();
						if ($y<1 || $y>16) $this->error();
						$d = [];
						for($yy=0;$yy<$y;$yy++) {
							for($xx=0;$xx<$x;$xx++) {
								$d[$yy][$xx] = $this->getByte();
							}
						}
						$this->printer->doGS_51($m,$x,$y,$d);
					break;
				case 0x54:
						$n = $this->getByte();
						if (!in_array($n,[0,1,0x40,0x41])) $this->error();
						$this->printer->doGS_54($n);
					break;
				case 0x56:
						$m = $this->getByte(); $n=0;
						if (!in_array($m,[0,1,0x30,0x31,0x41,0x42,0x61,0x62,0x67,0x68])) $this->error();
						if (!in_array($m,[0,1,0x30,0x31])) $n = $this->getByte();
						$this->printer->doGS_56($m, $n);
					break;
				case 0x57: $this->printer->doGS_57($this->getWord()); break;
				case 0x5C: $this->printer->doGS_5C($this->getWord()); break;
				case 0x5E:
						$r = $this->getByte();
						$t = $this->getByte();
						$m = $this->getByte();
						if (!in_array($m,[0,1])) $this->error();
						$this->printer->doGS_5E($r, $t, $m);
					break;
				case 0x61: $this->printer->doGS_61($this->getByte()); break;
				case 0x62: $this->printer->doGS_62($this->getByte()); break;
				case 0x63: $this->printer->doGS_63(); break;
				case 0x66:
						$n = $this->getByte();
						if (!in_array($n,[0,1,2,3,4,0x40,0x41,0x42,0x43,0x44,0x61,0x62])) $this->error();
						$this->printer->doGS_66($n);
					break;
				case 0x67:
						if (!in_array($this->getByte(),[0x30,0x32])) $this->error();
						$m = $this->getByte();
						if ($m!=0) $this->error();
						$this->printer->doGS_67($m,$this->getWord());
					break;
				case 0x68: $this->printer->doGS_68($this->getByte()); break;
				case 0x6A: $this->printer->doGS_6A($this->getByte()); break;
				case 0x6B:
						$m = $this->getByte();
						if (!in_array($m,[0,1,2,3,4,5,6,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x47,0x48,0x49,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F])) $this->error();
						if ($m > 0x40) {
							$d = [];
							$n = $this->getByte();
							for($i=0;$i<$n;$i++) $d[]=$this->getByte();
						} else {
							$d = $this->getNullTerminatedArray();
							$n = count($d)-1;
						}
						$this->printer->doGS_6B($m, $n, $d);
					break;
				case 0x72:
						$n = $this->getByte();
						if (!in_array($n,[1,2,4,0x31,0x32,0x34])) $this->error();
						$this->printer->doGS_72($n);
					break;
				case 0x76:
						if (!in_array($this->getByte(),[0x30])) $this->error();
						$m = $this->getByte();
						if (!in_array($m,[0,1,2,3,0x30,0x31,0x32,0x33])) $this->error();
						$x = $this->getWord();
						$y = $this->getWord();
						if ($y>0x11FF) $this->error();
						$d=[];
						for($yy=0;$yy<$y;$yy++) {
							for($xx=0;$xx<$x;$xx++) {
								$d[$yy][$xx] = $this->getByte();
							}
						}
						$this->printer->doGS_76($m, $x, $y, $d);
					break;
				case 0x77:
						$n = $this->getByte();
						if (!in_array($n,[1,2,3,4,5,6,7,8,0x44,0x45,0x46,0x47,0x48,0x49,0x4A,0x4B,0x4C])) $this->error();
						$this->printer->doGS_77($n);
					break;
				case 0x7A:
						if (!in_array($this->getByte(),[0x30])) $this->error();
						$this->printer->doGS_7A($this->getByte());
					break;
			default: $this->error();
		}
	}

	private function parseDLE_EOT() {
		$n = $this->getByte();
		$a = 0;
		if(!in_array($n,[1,2,3,4,7,8,18])) $this->error();
		if(in_array($n,[7,8,18])) $a = $this->getByte();
		$this->printer->doDLE_EOT($n, $a);
	}

	private function parseDLE_ENQ() {
		$n = $this->getByte();
		if(!in_array($n,[0,1,2])) $this->error();
		$this->printer->doDLE_ENQ($n);
	}

	private function parseDLE_DC4() {
		$fn = $this->getByte();
		switch ($fn) {
			case 0x01:
				$m = $this->getByte();
				if(!in_array($m,[0,1])) $this->error();
				$t = $this->getByte();
				if(!in_array($t,[1,2,3,4,5,6,7,8])) $this->error();
				$this->printer->doDLE_DC4($fn,$m,$t);
				break;
			case 0x02:
				$a = $this->getByte();
				if($a!=1) $this->error();
				$b = $this->getByte();
				if($b!=8) $this->error();
				$this->printer->doDLE_DC4($fn,$a,$b);
				break;
			case 0x03:
				$a = $this->getByte();
				$n = $this->getByte();
				$r = $this->getByte();
				$t1 = $this->getByte();
				$t2 = $this->getByte();
				$this->printer->doDLE_DC4($fn,$a,$n,$r,$t1,$t2);
				break;
			case 0x07:
				$m = $this->getByte();
				if(!in_array($m,[1,2,4,5])) $this->error();
				$this->printer->doDLE_DC4($fn,$m);
			case 0x08:
				$d1 = $this->getByte(); $d2 = $this->getByte();
				$d3 = $this->getByte(); $d4 = $this->getByte();
				$d5 = $this->getByte(); $d6 = $this->getByte(); $d7 = $this->getByte();
				if ("01032001060208"!=sprintf('%02X%02X%02X%02X%02X%02X%02X',$d1,$d2,$d3,$d4,$d5,$d6,$d7)) $this->error();
				$this->printer->doDLE_DC4($fn,$d1,$d2,$d3,$d4,$d5,$d6,$d7);
			default: $this->error(); break;
		}
		$n = $this->getByte();
		if(!in_array($n,[1])) $this->error();
		$m = $this->getByte();
		if(!in_array($m,[0,1])) $this->error();
		$t = $this->getByte();
		if(!in_array($t,[0,1,2,3,4,5,6,7,8])) $this->error();
		$this->printer->doDLE_DC4($n,$m,$t);
	}

}
