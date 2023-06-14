<?php
$fn = @$argv[1];
if (!!$fn) {
  $parser = new TexParser(str_split(file_get_contents($fn)));
  $parser->parse();
} else die("File missing.");

class TexParser {
	protected $contents = [];
	protected $ptr = 0;
	protected $layer = 0;
	protected $output = null;

	public function __construct($contents) {
		$key = 0;
		foreach ($contents as $v) {
			if (ord($v) == 13) continue;
			$this->contents[$key] = ord($v);
			$key++;
		}
		$this->ptr = 0;
		$this->output = fopen("output.tex", "w");
	}

	public function __destruct() {
		fclose($this->output);
	}

	private function write($str) {
		fwrite($this->output, $str);
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

	private function tryNextByte($predicate) {
		$ret = $this->getByte();
		if ($predicate($ret)) return $ret;
		else $this->prev();
		return false;
	}

	private function skipSpaces() {
		while ($this->tryNextByte(function($v) { return $v <= 32 && $v != 10; }));
	}

	// Reads every printable character + spaces and tab (stops at newline)
	private function getString() {
		$ret = '';
		while (!$this->eof()) {
			$ch = $this->tryNextByte(function($v) { return $v >= 32 || $v == 9; }); //Horizontal tab
			if (!!$ch) $ret .= chr($ch);
			else break;
		}
		return $ret;
	}

	// Reads every printable character (stops at space, hashtag and backslash) OUTSIDE of []
	private function getExteriorString() {
		$ret = '';
		while (!$this->eof()) {
			$ch = $this->tryNextByte(function($v) { return $v > 32 && $v != ord('#') && $v != ord('\\'); });
			if (!!$ch) $ret .= chr($ch);
			else break;
		}
		return $ret;
	}

	// Reads every printable character (stops at space, hashtag and backslash) INSIDE of [] and executes eventual commands
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
				$ret = "\\date{".$this->readArg()."}\n\\maketitle";
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
				$style = $this->readArg(); //map from style to latex (bullet->itemize, numbered->enumerate...)
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
		//TODO: if command needs //
		if ($this->tryNextByte(function($v) { return $v == 10; })) $ret . "\\\\\n";
		return $ret;
	}

	public function parse($printer=null) {
		while(!$this->eof()) {
			$code = chr($this->getByte());
			// if ($code == "\n") print(" SIUM ");
			switch ($code) {
			 	case '[':
			 		if ($this->layer == 0)
			 			$this->write("\\documentclass{article}\n\\begin{document}");
			 		$this->layer++;
			 		break;
			 	case ']':
			 		$this->layer--;
			 		if ($this->layer == 0)
			 			$this->write("\\end{document}");
			 		break;
			 	case '\\':
			 		$this->write($this->parse_command());
			 		break;
			 	case '#':
			 		$this->write(" % " . $this->getString());
			 		break;
			 	case "\n":
			 		$this->write("\n");
			 	break;
			 	default:
			 		break;
			}
		}
	}
}
