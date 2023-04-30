<?php
$fn = @$argv[1];
if (!!$fn) {
  $parser = new TexParser(file_get_contents($fn));
  $parser->parse();
} else die("File missing.");

class TexParser {
	protected $contents = [];
	protected $ptr = 0;
	protected $layer = 0;

	public function __construct($contents) {
		$this->contents = array_values(unpack('C*', $contents));
		$this->ptr = 0;
	}

	private function eof() {
		return $this->ptr >= count($this->contents);
	}

	private function prev() {
		return $this->ptr--;
	}

	private function next() {
		return $this->contents[$this->ptr++];
	}

	private function getByte() {
		if (!$this->eof()) return $this->next();
		else die("Tried to read after EOF, fail.");
	}

	private function tryNextByte($func) {
		$ret = $this->getByte();
		if ($func($ret)) return $ret;
		else $this->prev();
		return false;
	}

	private function skipSpaces() {
		while ($this->tryNextByte(function($v) { return $v <= 32 && $v != 10; }));
	}

	private function getString() {
		$ret = '';
		while (!$this->eof()) {
			$ch = $this->tryNextByte(function($v) { return $v >= 32; });
			if (!!$ch) $ret .= chr($ch);
			else break;
		}
		return $ret;
	}

	private function getExteriorString() { //not between []
		$ret = '';
		while (!$this->eof()) {
			$ch = $this->tryNextByte(function($v) { return $v > 32 && $v != ord('#') && $v != ord('\\'); });
			if (!!$ch) $ret .= chr($ch);
			else break;
		}
		return $ret;
	}

	private function getArgumentString() {
		$ret = '';
		while (!$this->eof()) {
			$ch = $this->tryNextByte(function($v) { return $v >= 32; });
			if (!!$ch)
				if ($ch == ord(']')) {
					$this->layer--;
					break;
				} elseif ($ch == ord('\\'))
					$ret .= $this->parse_command();
				else
					$ret .= chr($ch);
			else
				break;
		}
		return $ret;
	}

	private function readArg() {
		$this->skipSpaces();
		$arg = '';
		$c = $this->tryNextByte(function($v) {return $v == ord('['); });
		if (!!$c){
			$this->layer++;
			$arg .= $this->getArgumentString();
		} else {
			$c = $this->tryNextByte(function($v) {return $v == ord('\\'); });
			if (!!$c)
				$arg .= $this->parse_command();
			$arg .= $this->getExteriorString();
		}
		return $arg;
	}

	private function parse_command() {
		$code = $this->getExteriorString(); //print($code. " -> ");
		$ret = '';
		switch ($code) {
			case "h1":
				$ret = "\\title{".$this->readArg()."}";
				break;
			case "h2":
				$ret = "\\author{".$this->readArg()."}";
				break;
			case "h3":
				$ret = "\\date{".$this->readArg()."}";
				break;
			case "sect":
				$ret = "\\section{".$this->readArg()."}";
				break;
			case "b":
				$ret = "\\textbf{".$this->readArg()."}";
				break;
			case "i":
				$ret = "\\emph{".$this->readArg()."}";
				break;
			case "p":
				$style = $this->readArg(); //if style not supported then style=justified
				$ret = $this->readArg();
				if (!$ret) {
					$ret = $style;
					$style = "justified";
				}
				break;
			case "l":
				$style = $this->readArg(); //map da style al corrispettivo latex (bullet->itemize, numbered->enumerate...)
				$elems = $this->readArg();
				if (!$elems) {
					$elems = $style;
					$style = "bullet";
				}
				$ret = "\\begin{itemize}\n\item " . implode("\n\item ", explode(";;", $elems)) . "\n\\end{itemize}";
				break;
			default:
				$ret = $code . "\n";
				break;
		}
		return $ret;
	}

	public function parse($printer=null) {
		while(!$this->eof()) {
			$code = chr($this->getByte());
			switch ($code) {
			 	case '[':
			 		if ($this->layer == 0)
			 			print("\\begin{document}\n");
			 		$this->layer++;
			 		break;
			 	case ']':
			 		$this->layer--;
			 		if ($this->layer == 0)
			 			print("\\end{document}");
			 		break;
			 	case '\\':
			 		print($this->parse_command() . "\n");
			 		break;
			 	case '#':
			 		print(" % " . $this->getString() . "\n");
			 		break;
			 	case '\n':
			 		print("\n");
			 		break;
			 	default:
			 		break;
			}
		}
	}
}
