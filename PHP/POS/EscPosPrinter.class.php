<?php

require_once "libs/TCPDFBarcode.class.php";
require_once "libs/QRCode.class.php";
require_once "libs/PDF417.class.php";
require_once "libs/Datamatrix.class.php";

class EscPosPrinter{
	private $color=[NULL, NULL];
	private $canvas=NULL;
	private $buffer=NULL;
	private $graphics_buffer=NULL;
	private $fontA=NULL;
	private $fontB=NULL;
	private $font=NULL;
	private $horizontalTab=[];
	private $lineSpacing=0;
	private $count=0; //TODO remove
	private $printMode=NULL;
	private $printArea=NULL;
	private $pdf417=NULL;
	private $qrcode=NULL;
	private $datamatrix=NULL;
	private $barcode=NULL;
	private $currentXY=NULL;
	
	public function __construct() {
		$this->currentXY=(object)[];
		$this->currentBarcodeSize=(object)[];
		$this->fontA = (object)["nameRegular"=>realpath("fonts/escpos-regular.ttf"),"nameBold"=>realpath("fonts/escpos-bold.ttf"),"size"=>16,"width"=>12,"height"=>24];
		$this->fontB = (object)["nameRegular"=>realpath("fonts/escpos-regular.ttf"),"nameBold"=>realpath("fonts/escpos-black.ttf"),"size"=>11,"width"=>9,"height"=>17];
		$this->printMode = (object)[];
		$this->printArea = (object)[];
		for($i=1;$i<32;$i++) $this->horizontalTab[] = 8*$i;
		$this->doESC_40();
		$this->setXY(0,0);
	}

	public function __destruct()	{ if(!!$this->canvas->img) imagedestroy($this->canvas->img); }
	public function export($fn)		{ imagepng($this->canvas->img, $fn); }
	private function dump($d)			{ return json_encode($this->hexdump($d)); }

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

	private function init($w,$h) {
		if((!empty($this->canvas)) && (!!$this->canvas->img)) imagedestroy($this->canvas->img);
		$this->doESC_53();
		$this->doGS_42(0);
		$this->canvas = (object)$this->createImage($w, $h, false); $this->color = $this->canvas->color;
		
		$this->doESC_2D(1);
		$this->doESC_21(0);
		$this->doGS_4C(0);
		$this->doESC_47(0);
		$this->doESC_56(0);
		$this->doESC_61(0);
		$this->doESC_7B(0);
		$this->doESC_32();
		
		$this->graphics_buffer = $this->createImage($w, $h);
		$this->buffer = $this->createImage($w, max($this->font->height, $this->font->width)*48);
		$this->buffer->w = 0; $this->buffer->h = 0;

		$this->pdf417=(object)["cols"=>0, "rows"=>0, "w"=>1, "h"=>4, "ecl"=>1, "truncated"=>0, "content"=>[]];
		$this->qrcode=(object)["mode"=>0x32, "module"=>3, "ecl"=>0, "content"=>[]];
		$this->datamatrix=(object)["mode"=>"S", "cols"=>16, "rows"=>16, "module"=>3, "content"=>[]];
		$this->barcode=(object)["hri"=>0, "font"=>$this->font, "w"=>2, "h"=>30];
	}

	private function checkCanvas($h) {
		$h += $this->lineSpacing * $this->printMode->fontZoom[1];
		if ($h>$this->canvas->height)
			$this->cropCanvas(max($h,$this->canvas->height));
	}

	private function cropCanvas($h) {
		$canvas=$this->canvas;
		$this->canvas = $this->createImage($canvas->width, $h, false); $this->color = $this->canvas->color;
		imagecopy($this->canvas->img, $canvas->img, 0, 0, 0, 0, $canvas->width, $h);
		imagedestroy($canvas->img);
	}

	private function setXY($x,$y) {
		$this->currentXY->x=$x;
		$this->currentXY->y=$y;
	}

	private function stringXY($font,$x,$y,$text) {
		$ut = !!$this->printMode->underline ? $this->printMode->underlineThickness : 0;
		$box = imagettfbbox($font->size, 0, $font->name, $text);
		$w = max($font->width, $box[4]-$box[6]); $h = max($font->height, $box[3]-$box[5] + $ut);

		if (!!$this->printMode->ninetycw) {
			$a = $w; $w = $h; $h = $a;
		}

		if ($w > $this->printArea->w) {
			$total = ceil($w / $this->printArea->w);
			$part_size = ceil(strlen($text)/$total);
			for ($i=0; $i < $total; $i++) {
				$part = substr($text, 0, $part_size);
				$this->checkCanvas($y+$this->lineSpacing*$i);
				$this->stringXY($font,$x,$y+$this->lineSpacing*$i,$part);
				$text = trim(substr($text, $part_size));
				$this->doLF();
			}
			return;
		}

		if (!!$this->printMode->ninetycw) {
			$a = $w; $w = $h; $h = $a;
		}

		$obj = $this->createImage($w, $h, false); $img = $obj->img; $color = $obj->color;
		imagettftext($img, $font->size, 0, -$box[6], imagesy($img)-$box[3]-$ut, $color[1], $font->name, $text);

		if (!$this->printMode->ninetycw)
			for ($i=1; $i <= $ut; $i++)
				ImageLine($img, 0, imagesy($img) - $i, $box[4], imagesy($img) - $i, $color[1]);
		if (!!$this->printMode->strike) {
			$lineH = ceil((imagesy($img) - $ut) / 3);
			ImageLine($img, 0, imagesy($img)-($lineH+1), $box[4], imagesy($img)-($lineH+1), $color[1]);
			$lineH *= 2;
			ImageLine($img, 0, imagesy($img)-($lineH-1), $box[4], imagesy($img)-($lineH-1), $color[1]);
		}
		if (!!$this->printMode->ninetycw) $img = imagerotate($img, -90, 0);
		if (!!$this->printMode->upsideDown) $img = imagerotate($img, 180, 0);

		if (!!$this->printMode->ninetycw) {
			$a = $w; $w = $h; $h = $a;
		}

		$this->currentXY->x = $x; $this->currentXY->y = $y;
		$w_r = $w * $this->printMode->fontZoom[0]; $h_r = $h * $this->printMode->fontZoom[1];
		$this->buffer->w += $w_r;
		$this->buffer->h = max($this->buffer->h, $h_r);
		imagecopyresized($this->buffer->img, $img, $this->buffer->w - $w_r, imagesy($this->buffer->img)-$h_r, 0, 0, $w_r, $h_r, $w, $h);
		
		$this->count++;
		imagepng($img, sprintf("buffers\\img%s.png", str_pad($this->count,2,0,STR_PAD_LEFT)));
		imagepng($this->buffer->img, sprintf("buffers\\buffer%s.png", str_pad($this->count,2,0,STR_PAD_LEFT)));
		
		imagedestroy($img);
	}

	private function feedXY($x,$y) {
		$buffer=$this->buffer;
		$x = $this->justifiedX($x, $buffer->w);
		$this->checkCanvas($y + $buffer->h * ($this->printMode->fontZoom[1] + 1));
		imagecopy($this->canvas->img, $buffer->img, $x, $y, 0, imagesy($buffer->img) - $buffer->h, $buffer->w, $buffer->h);
		imagefilledrectangle($buffer->img, 0, 0, imagesx($buffer->img), imagesy($buffer->img), $buffer->color[0]);
		$this->currentXY->x+=$buffer->w;
		$this->buffer->w = 0; $this->buffer->h = 0;
	}

	private function createImage($w, $h, $alpha = true) {
		$img = imagecreate($w, $h);
		if ($this->printMode->negative) {
			$color = [imagecolorallocate($img, 0, 0, 0), imagecolorallocate($img, 255, 255, 255)];
		} else {
			$color = [imagecolorallocate($img, 255, 255, 255), imagecolorallocate($img, 0, 0, 0)];
		}
		imagefilledrectangle($img, 0, 0, $w, $h, $color[0]);
		if ($alpha) imagecolortransparent($img, $color[$this->printMode->negative?1:0]);
		return (object)["img"=>$img, "color"=>$color, "width"=>$w, "height"=>$h];
	}

	private function copyToCanvas($src) {
		$this->checkCanvas($this->currentXY->y + imagesy($src));
		imagecopy($this->canvas->img, $src, $this->justifiedX($this->currentXY->x, imagesx($src)), $this->currentXY->y, 0, 0, imagesx($src), imagesy($src));
		$this->currentXY->y += imagesy($src);
	}
	
	private function justifiedX($x, $w, $max=NULL, $just=NULL) {
		if (!$max)	$max	= $this->printArea->w;
		if (!$just)	$just = $this->printMode->justification;
		if ($just == "left" && !$this->printMode->upsideDown) return $x;
		$x += ($max - $w);
		$x /= ($just == "right" || !!$this->printMode->upsideDown) ? 1 : 2;
		return ceil($x);
	}

	private function bitmapXY($xx,$yy,$bitmap,$t,$image_obj=NULL) {
		$w=count($bitmap[0]);
		$h=count($bitmap);
		
		if (!$image_obj) {
			$this->checkCanvas($yy+$h*$t[1]);
			$img = $this->canvas->img;
			$color = $this->canvas->color;
			$xx = $this->justifiedX($xx, $w * 8 * $t[0]);
		} else {
			$img = $image_obj->img;
			$color = $image_obj->color;
		}

		for($y=0;$y<$h;$y++)
			for($x=0;$x<$w;$x++)
				foreach(str_split(str_pad(decbin($bitmap[$y][$x]),8,0,STR_PAD_LEFT)) as $k=>$b)
					for($t1=0;$t1<$t[1];$t1++)
						for($t0=0;$t0<$t[0];$t0++)
							imagesetpixel($img, $t0+$xx+8*$x*$t[0]+($k*$t[0]), $t1+$yy+$y*$t[1], $color[$b]);

		if (!$image_obj) {
			$this->currentXY->x = $this->printArea->x;
			$this->currentXY->y = $yy+$h*$t[1]+1;
		}
		//else {
		//	imagepng($img, sprintf("buffers\\image%s.png", str_pad($this->count,2,0,STR_PAD_LEFT)));
		// }
	}

	/****
		????? cioè ?
		The position at which subsequent characters are to be printed for raster bit image is specified by HT(Horizontal Tab) ESC $ (Set absolute print position), ESC ＼ (Set relative print position), and GS L (Ste left margin).
		If the position at which subsequent characters are to be printed is not a multiple of 8, print speed may decline.
	*/

	private function bitmapYX($xx,$yy,$bitmap,$t) {
		$h=count($bitmap[0]);
		$w=count($bitmap);
		$xx = $this->justifiedX($xx, $w*$t);
		$this->checkCanvas($yy+$h);

		if (!!$this->printMode->upsideDown) {
			$reverse_bitmap = [];
			for($x=0;$x<$w;$x++)
				$reverse_bitmap[$w-1-$x] = array_reverse($bitmap[$x]);
			$bitmap = $reverse_bitmap;
		}

		for($x=0;$x<$w;$x++)
			for($y=0;$y<$h;$y++) {
				$arr = str_split(str_pad(decbin($bitmap[$x][$y]),8,0,STR_PAD_LEFT));
				if (!!$this->printMode->upsideDown) $arr = array_reverse($arr);
				foreach($arr as $k=>$b)
					for($t0=0;$t0<$t;$t0++)
						switch($h) {
							case 1:
									imagesetpixel($this->canvas->img, $t0+$xx+$x*$t, $yy+$k*2, $this->color[$b]);
									imagesetpixel($this->canvas->img, $t0+$xx+$x*$t, $yy+$k*2+1, $this->color[$b]);
								break;
							case 3:
									imagesetpixel($this->canvas->img, $t0+$xx+$x*$t, $yy+ceil(0.66*($k+$y*8)), $this->color[$b]);
								break;
						}
			}
	}


	private function printCode(&$code) {
		print_r($code->content);
		$bitmap = $code->content->bcode;
		$rows = $code->content->num_rows; $cols = $code->content->num_cols;
		$t_w = property_exists($code, "module") ? $code->module : $code->w;
		$t_h = property_exists($code, "module") ? $code->module : $code->h;
		$xx = $this->justifiedX($this->currentXY->x, $cols * $t_w); $yy = $this->currentXY->y;
		
		$this->checkCanvas($yy+$cols*$t_h);

		if (!!$this->printMode->upsideDown) {
			$reverse_bitmap = [];
			for($y=0;$y<$rows;$y++)
				$reverse_bitmap[$rows-1-$y] = array_reverse($bitmap[$y]);
			$bitmap = $reverse_bitmap;
		}

		for($y=0;$y<$rows;$y++)
			for($x=0;$x<$cols;$x++) {
				$b = $bitmap[$y][$x];
				for ($t0=0; $t0 < $t_h; $t0++)
					for ($t1=0; $t1 < $t_w; $t1++)
						imagesetpixel($this->canvas->img, $t1+$xx+$x*$t_w, $t0+$yy+$y*$t_h, $this->color[$b]);
				}

		$this->currentXY->x = $this->printArea->x;
		$this->currentXY->y = $yy+$cols*$t_h+1;
		$code->content = [];
	}


	private function paperCut() {
		$y=$this->currentXY->y;
		for($x=0;$x<$this->canvas->width;$x+=16)
			for($k=0;$k<8;$k++)
				imagesetpixel($this->canvas->img, $x+$k, $y, $this->color[1]);
		$this->cropCanvas($this->currentXY->y+$this->lineSpacing);
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* PRINT FUNCTIONS
			(insert string in buffer), LF, FF, CR
			ESC FF, ESC J, ESC K, ESC d, ESC e	
	*/

	public function doString($text) {
		#printf("\n(string)	- %s\n",$text);
		$this->stringXY($this->font, $this->currentXY->x, $this->currentXY->y, $text);
	}
	
	public function doLF() {
		#printf("\nLF				- Print and line feed\n");
		$this->feedXY($this->currentXY->x,$this->currentXY->y);
		$this->currentXY->x = $this->printArea->x;
		$this->currentXY->y += $this->lineSpacing * $this->printMode->fontZoom[1];
		$this->checkCanvas($this->currentXY->y);
	}

	public function doFF()				{ printf("\nFF			- Print end position label to start printing\n"); }
	public function doCR()				{ printf("\nCR			- Print and carriage return\n");	}
	public function doESC_0C()		{ printf("\nESC FF	- Print data in page mode\n");	}
	
	public function doESC_4A($n) {
		#printf("\nESC J		- Print and feed paper\n%02X\n",$n);
		$this->currentXY->y += $n;
	}
	
	public function doESC_4B($n) {
		#printf("\nESC K		- Print and reverse feed\n%02X\n",$n);
		$this->currentXY->y -= $n;
	}

	public function doESC_64($n) {
		#printf("\nESC d		 - Print and feed n lines\n%02X\n",$n);
		for($i=0; $i<$n; $i++) $this->doLF();
	}

	public function doESC_65($n) {
		#printf("\nESC e		 - Print and reverse feed n lines\n%02X\n",$n);
		$this->currentXY->y -= $n*$this->lineSpacing * $this->printMode->fontZoom[1];
		for($i=0; $i<$n; $i++) $this->doLF();
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* LINE SPACING FUNCTIONS
			ESC 2, ESC 3
	*/

	public function doESC_32() {
		#printf("\nESC 2		 - Select default line spacing\n");
		$this->doESC_33($this->font->height);
	}

	public function doESC_33($n) {
		#printf("\nESC 3		 - Set line spacing\n%02X\n",$n);
		$this->lineSpacing=$n;
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* PRINT POSITION FUNCTIONS
			HT
			ESC $, ESC D, ESC T, ESC W, ESC \, ESC a
			GS $, GS L, GS T, GS W, GS \
	*/

	public function doHT()					{ printf("\nHT			- Horizontal tab\n");	}
	public function doESC_24($n)		{ printf("\nESC $		- Set absolute print position\n%04X\n",$n); }
	public function doESC_44($d)		{ printf("\nESC D		- Set horizontal tab positions\n%s\n",$this->dump($d)); }
	public function doESC_54($n)		{ printf("\nESC T		- Select print direction in page mode\n%02X\n",$n); }
	public function doESC_57($x, $y, $dx, $dy) {
		printf("\nESC W		 - Set printing area in page mode\n%04X %04X		%04X %04X\n",$x,$y,$dx,$dy);
	}
	public function doESC_5C($n)		{ printf("\nESC \\	- Set relative print position\n%04X\n",$n); }

	public function doESC_61($n) {
		$this->printMode->justification = (!!($n & 2) ? "right" : (!!($n & 1) ? "center" : "left"));
		#printf("\nESC a		 - Select justification\n%s\n",$this->printMode->justification);
	}

	public function doGS_24($n)			{ printf("\nGS $		- Set absolute vertical print position in page made\n%04X\n",$n); }
	public function doGS_4C($n) {
		#printf("\nGS L		- Set left margin\n%d\n",$n);
		$this->printArea->w = $this->canvas->width;
		$this->printArea->x = $n;
		$this->currentXY->x = $n;
		$this->doGS_57($this->printArea->w - $this->printArea->x);
	}
	public function doGS_54($n)			{ printf("\nGS T		- Set print position to the beginning of print line\n%02X\n",$n); }
	public function doGS_57($n)	{
		#printf("\nGS W		- Set printing area width\n%d\n",$n);
		$this->printArea->w = max($this->font->width, $n); 
	}
	public function doGS_5C($n)			{ printf("\nGS \\		- Set relative vertical print position in page mode\n%04X\n",$n); }
	
	/* ------------------------------------------------------------------------------------------------------------------ */

	/* PAPER SENSOR & PANEL BUTTON FUNCTIONS
			ESC c 3, 4, 5
	*/
	public function doESC_63($fn, $n)	{ printf("\nESC c %d	 - Whatever\n%02X\n", $fn, $n); }
	
	/*
	public function doESC_63_3($n)	{ printf("\nESC c 3	 - Select paper sensor(s) to output paper end signals\n%02X\n",$n); }
	public function doESC_63_4($n)	{ printf("\nESC c 4	 - Select paper sensor(s) to stop printing\n%02X\n",$n); }
	public function doESC_63_5($n)	{ printf("\nESC c 5	 - Enable / disable panel buttons\n%02X\n",$n); }
	*/

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* MECHANISM CONTROL FUNCTIONS
			ESC <, ESC U, ESC i, ESC m
			GS V
	*/

	public function doESC_3C()			{ printf("\nESC <		 - Return home.\n",$n); }
	public function doESC_55()			{ printf("\nESC U		 - Turn unidirectional print mode on/off.\n",$n); }
	public function doESC_69()			{ printf("\nESC i		 - Execute paper full cut.\n",$n); }

	public function doESC_6D() {
		#printf("\nESC m		 - Execute paper partial cut.\n");
		$this->doGS_56(0);
	}

	public function doGS_56($m,$n=0) {
		#printf("\nGS V			- Select cut mode and cut paper\n%02X %02X\n",$m,$n);
		for($i=0; $i<$n; $i++) $this->doLF();
		$this->paperCut();
		$this->doLF();
	}
	
	/* ------------------------------------------------------------------------------------------------------------------ */

	/* STATUS FUNCTIONS
			DLE EOT
			ESC u, ESC v
			FS ( e
			GS a, GS j, GS r
	*/

	public function doDLE_EOT($n, $a)	{ printf("\nDLE_EOT	- Real-time status transmission\n%02X %02X\n",$n,$a); }
	public function doESC_75($n)			{ printf("\nESC u	- Transmit peripheral device status\n%02X\n",$n); }
	public function doESC_76()				{ printf("\nESC v	- Transmit paper sensor status\n%02X\n",$n); }
	public function doFS_28($f, $p, $d)	{
		printf("\FS ( %c	 - Set up and print the symbol\n%04X\n%s\n",$f,$p,$this->dump($d));
	}
	public function doGS_61($n)			{ printf("\nGS a			- Enable/Disable Automatic Status Back\n%02X\n",$n); }
	public function doGS_6A($n)			{ printf("\nGS j			- Enable/disable Automatic Status Back for ink\n%02X\n",$n); }
	public function doGS_72($n)			{ printf("\nGS r			- Transmit status\n%02X\n",$n); }
	
	/* ------------------------------------------------------------------------------------------------------------------ */

	/* MISC FUNCTIONS
			DLE ENQ, DLE DC4
			ESC ( Y, ESC =, ESC @, ESC L, ESC S, ESC p
			GS ( A, D, GS I, GS P, GS g 0, GS g 2, GS z 0
			
	*/

	public function doDLE_ENQ($n)		{ printf("\nDLE_ENQ	- Real-time request to printer\n%02X\n",$n); }
	public function doDLE_DC4($n,$m,$t) { /*Generate pulse*/ }
	public function doESC_3D($n)		{ printf("\nESC =		 - Set peripheral device\n%02X\n",$n); }

	public function doESC_40() {
		#printf("\nESC @		 - Initialize printer\n");
		// standard 80mm 3,15" 567
		$this->init(576,360);
	}

	public function doESC_4C() {
		#printf("\nESC L		 - Select page mode\n");
		$this->printMode->pageMode=true;
	}

	public function doESC_53() {
		#printf("\nESC S		 - Select standard mode\n");
		$this->printMode->pageMode=false;
	}
	
	public function doESC_70($m, $t1, $t2) { /*Generate pulse*/ }
	public function doGS_49($n)			{ printf("\nGS I			- Transmit printer ID\n%02X\n",$n); }
	public function doGS_50($x,$y)	{ printf("\nGS p			- Set horizontal and vertical motion units\n%02X %02X\n",$n,$m); }
	
	/* ------------------------------------------------------------------------------------------------------------------ */

	/* CHARACTER-RELATED FUNCTIONS
			CAN
			ESC SP, ESC !, ESC -, ESC E, ESC G, ESC M, ESC R, ESC V, ESC r, ESC t, ESC {
			GS !, GS B, GS b
	*/
	
	public function doCAN()				{ printf("\nCAN		- Cancel print data in page mode\n"); }
	public function doESC_20($n)		{ printf("\nESC SP	- Set right-side character spacing\n%02X\n",$n); }

	public function doESC_21($n) {
		#printf("\nESC !		 - Select print mode(s)\n%02X\n",$n);
		$this->printMode->underline		=  !!(($n>>7) & 1);
		$this->printMode->fontZoom		= [!!(($n>>5) & 1) ? 2 : 1, !!(($n>>4) & 1) ? 2 : 1];
		$this->printMode->emphasized	=  !!(($n>>3) & 1);
		$this->printMode->fontB				=  !!( $n	   & 1);
		$this->font										=  ($this->printMode->fontB ? $this->fontB : $this->fontA);
		$this->font->name							=  ($this->printMode->emphasized ? $this->font->nameBold : $this->font->nameRegular);
		/****
			In Standard mode, the character is enlarged in the paper feed direction when double-height mode is selected,
			and it is enlarged perpendicular to the paper feed direction when double-width mode is selected.
			However, when character orientation changes in 90° clockwise rotation mode, the relationship between double-height and double-width is reversed.
			in Page mode, double-height and double-width are on the character orientation.
		*/	
	}

	public function doESC_2D($n) {
		#printf("\nESC -		 - Turn underline mode on/off\n%02X\n",$n);
		$this->printMode->underlineThickness = $n & 3;
		$this->printMode->underline = !!$this->printMode->underlineThickness;
	}

	public function doESC_45($n) {
		#printf("\nESC E		 - Turn emphasized mode on/off\n%02X\n",$n);
		$this->printMode->emphasized = !!($n & 1);
		$this->font->name = ($this->printMode->emphasized ? $this->font->nameBold : $this->font->nameRegular);
	}

	public function doESC_4D($n) {
		#printf("\nESC M		 - Select character font\n%02X\n",$n);
		$this->printMode->fontB	= !!($n & 1);
		$this->font	= ($this->printMode->fontB ? $this->fontB : $this->fontA);
		$this->font->name = ($this->printMode->emphasized ? $this->font->nameBold : $this->font->nameRegular);
	}

	public function doESC_47($n) {
		#printf("\nESC G		 - Turn on/off double-strike mode\n%02X\n",$n);
		$this->printMode->strike = !!($n & 1);
	}

	public function doESC_52($n)		{ printf("\nESC R		 - Select an international character set\n%02X\n",$n); }
	
	public function doESC_56($n) {
		#printf("\nESC V		 - Turn 90deg clockwise rotation mode on/off\n%02X\n",$n);
		$this->printMode->ninetycw=!!($n & 1);
	}

	public function doESC_72($n)		{ printf("\nESC r		 - Select print color\n%02X\n",$n); }
	public function doESC_74($n)		{ printf("\nESC t		 - Select character code table\n%02X\n",$n); }

	public function doESC_7B($n) {
		#printf("\nESC {		 - Turns on/off upside-down printing mode\n%02X\n",$n);
		$this->printMode->upsideDown=!!($n & 1);
	}

	public function doGS_21($n) {
		#printf("\nGS !			- Select character size\n%02X\n",$n);
		$this->printMode->fontZoom=[1+(($n & 0x70)>>4), 1+($n & 0x07)];
	}

	public function doGS_42($n) {
		#printf("\nGS B			- Turn white/black reverse printing mode\n%02X\n",$n);
		$this->printMode->negative = !!($n & 1);
	}

	public function doGS_62($n)		{ printf("\nGS b		 - Turns on/off smooth printing mode\n%02X\n",$n); }

	/* USER-DEFINED CHARACTERS FUNCTIONS
			ESC %, ESC &, ESC ?
	*/

	public function doESC_25($n)		{ printf("\nESC %		 - Select/cancel user-defined character set\n%02X\n",$n); }
	public function doESC_26($y, $c1, $c2, $x, $d) { printf("\nESC &		 - Define user-defined characters\n%02X		%02X - %02X\n%s\n%s\n",$y,$c1,$c2,$this->dump($x),$this->dump($d)); }
	public function doESC_3F($n)		{ printf("\nESC ?		 - Cancel user-defined characters\n%02X\n",$n); }
	
	/* ------------------------------------------------------------------------------------------------------------------ */

	/* TWO-DIMENSIONAL CODE FUNCTIONS
			GS ( k
	*/

	public function doGS_28_6B($fn, $d) {
		switch ($fn) {

			/** PDF417 SECTION START **/
			case 65:		$this->pdf417->cols			= $d[0];															break;
			case 66:		$this->pdf417->rows			= $d[0];															break;
			case 67:		$this->pdf417->w					= $d[0];															break;
			case 68:		$this->pdf417->h					= $d[0];															break;
			case 69:		$this->pdf417->ecl				= $d[0] == 48 ? $d[1] & (~0x30) : -1;	break;
			case 70:		$this->pdf417->truncated	= !!($d[0] & 1);											break;
			case 80:		array_shift($d); $this->pdf417->content = $d;										break;
			case 81:
					$pdf417 = "";
					for ($i=0; $i < count($this->pdf417->content); $i++) $pdf417.=chr($this->pdf417->content[$i]);
					$pdf417_obj = new PDF417($pdf417, $this->pdf417->ecl, $this->pdf417->cols, $this->pdf417->rows, $this->pdf417->truncated);
					$this->pdf417->content = (object)$pdf417_obj->getBarcodeArray();
					$this->printCode($this->pdf417);
				break;
			/** PDF417 SECTION END **/

			/** QR CODE SECTION START **/
			case 165:	$this->qrcode->mode = $d[1];										break;
			case 167:	$this->qrcode->module = max(1, $d[0]);					break;
			case 169:	$this->qrcode->ecl = $d[0] & (~0x30);						break;
			case 180:	array_shift($d); $this->qrcode->content = $d;		break;
			case 181:
					$qr = "";
					for ($i=0; $i < count($this->qrcode->content); $i++) $qr.=chr($this->qrcode->content[$i]);
					$qr_obj = new QRcode($qr, ["L", "M", "Q", "H"][$this->qrcode->ecl]);
					$this->qrcode->content = (object)$qr_obj->getBarcodeArray();
					$this->printCode($this->qrcode);
				break;
			/** QR CODE SECTION END **/

			/** DATAMATRIX SECTION START **/
			case 666:	$this->datamatrix->mode = !!($d[0] & 1) ? "R" : "S";
									$this->datamatrix->cols = $d[1];
									$this->datamatrix->rows = $d[2];									break;
			case 667:	$this->datamatrix->module = max(2, $d[0]);				break;
			case 680:	array_shift($d); $this->datamatrix->content = $d;	break;
			case 681:
					$datamatrix = "";
					for ($i=0; $i < count($this->datamatrix->content); $i++) $datamatrix.=chr($this->datamatrix->content[$i]);
					$datamatrix_obj = new Datamatrix($datamatrix, $this->datamatrix->mode, $this->datamatrix->cols, $this->datamatrix->rows);
					$this->datamatrix->content = (object)$datamatrix_obj->getBarcodeArray();
					$this->printCode($this->datamatrix);
				break;
			/** DATAMATRIX SECTION END **/

			default: printf("\nGS ( k	 - Set up and print the symbol\n%d\n%s\n",$fn,$this->dump($d)); break;
		}
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* MULTIPLE TYPE FUNCTIONS
			GS (
	*/

	public function doGS_28($f, $p, $d)	{
		switch ($f) {
			case 0x4C:
					$fn = $d[1]; for ($i=0; $i < 2; $i++) array_shift($d);
					$this->doGS_28_4C($fn, $d);
				break;
			case 0x6B:
					$cn = $d[0]; $fn = $d[1]; $fn += 100*($cn & (~0x30));
					for ($i=0; $i < 2; $i++) array_shift($d);
					$this->doGS_28_6B($fn, $d);
				break;
			default:
					printf("\nGS ( %c	 - Set up and print the symbol\n%04X\n%s\n",$f,$p,$this->dump($d));
				break;
		}
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* BARCODE FUNCTIONS
			GS H, GS f, GS h, GS k, GS w
	*/

	public function doGS_48($n)	{
		#printf("\nGS H - Select printing position of HRI characters\n%02X\n",$n);
		$this->barcode->hri=$n & (~0x30);
	}

	public function doGS_66($n) {
		#printf("\nGS f - Select font for HRI characters\n%02X\n",$n);
		$this->barcode->font = !!$n ? $this->fontB : $this->fontA;
		$font	= $this->barcode->font;
		$this->barcode->font->name =  ($this->printMode->emphasized ? $font->nameBold : $font->nameRegular);
	}
	
	public function doGS_68($n) {
		#printf("\nGS h			- Select bar code height\n%02X\n",$n);
		$this->barcode->h=max(1, $n);
	}

	public function doGS_6B($m, $n, $d) {
		switch ($m) {
			case 0: case 0x41: $type = "UPCA"; break;
			case 1: case 0x42: $type = "UPCE"; break;
			case 2: case 0x43: $type = "EAN13"; break;
			case 3: case 0x44: $type = "EAN8"; break;
			case 4: case 0x45: $type = "C39"; break;
			case 5: case 0x46: $type = "I25"; break;
			case 6: case 0x47: $type = "CODABAR"; break;
			case 0x48: $type = "C93"; break;
			case 0x49: $type = "C128"; break;
			//TODO finish this
			default: printf("\nGS k			- Print bar code\n%02X		%02X\n%s\n",$m,$n,$this->dump($d)); break;
		}
		$data = "";
		for ($i=0; $i < $n; $i++) $data.=chr($d[$i]);
		$code_obj = new TCPDFBarcode($data, $type);
		$code_img = $code_obj->getBarcodePngData($this->barcode->w, $this->barcode->h);
		if (!!$code_img) {
			$src = imagecreatefromstring($code_img);

			switch ($this->barcode->hri) {
				case 1:
				case 3:
						$box = imagettfbbox($this->font->size, 0, $this->font->name, $data);
						$w = max($this->font->width, $box[4]-$box[6]);
						$this->stringXY($this->font, $this->justifiedX($this->currentXY->x, $w, imagesx($src), "center"), $this->currentXY->y, $data);
						$this->doLF();
						break;
				default: break;
			}

			if (!!$this->printMode->upsideDown) $src = imagerotate($src, 180, 0);
			$this->copyToCanvas($src);
			
			switch ($this->barcode->hri) {
				case 2:
				case 3:
						$box = imagettfbbox($this->font->size, 0, $this->font->name, $data);
						$w = max($this->font->width, $box[4]-$box[6]);
						$this->stringXY($this->font, $this->justifiedX($this->currentXY->x, $w, imagesx($src), "center"), $this->currentXY->y, $data);
						$this->doLF();
						break;
				default: break;
			}
		}
		else print("\nBarcode not created!\n");
	}

	public function doGS_77($n) {
		#printf("\nGS w			- Set bar code width\n%02X\n",$n);
		$this->barcode->w=max(1, $n);
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	/* BIT IMAGE FUNCTIONS
			ESC *,
			FS p, FS q
			GS ( L, GS *, GS /, GS D, GS Q 0, GS v 0
	*/

	public function doESC_2A($m, $n, $d) {
		#printf("\nESC *		 - Select bit-image mode\n%02X		%04X\n%s\n",$m,$n,$this->dump($d));
		$this->bitmapYX($this->currentXY->x, $this->currentXY->y, $d, 2 - ($m & 1));
	}
	
	public function doFS_70($n, $m)		{ printf("\nFS p			- Print NV bit image\n%02X %02X\n",$n,$m); }
	public function doFS_71($n, $x, $y, $d)	{
		printf("\nFS q			- Define NV bit image\n%s\n%s\n%s\n",$n,$this->dump($x),$this->dump($y),$this->dump($d));
	}

	public function doGS_28_4C($fn, $d) {
		switch ($fn) {
			case 0x70:
					$tone = $d[0]; $t_x = $d[1]; $t_y = $d[2]; $color = $d[3];
					$y = ($d[6] | ($d[7] << 8)); $x = ceil((count($d) - 10)/$y);
					for ($i=0; $i < 8; $i++) array_shift($d);
							
					$bitmap = [];
					for ($yy=0;$yy<$y;$yy++)
						for ($xx=0;$xx<$x;$xx++)
							$bitmap[$yy][$xx] = $d[$xx+$x*$yy];
					
					imagedestroy($this->graphics_buffer->img);
					$this->graphics_buffer = $this->createImage($x*8*$t_x, $y*$t_y);
					$this->bitmapXY(0, 0, $bitmap, [$t_x, $t_y], $this->graphics_buffer);
				break;
			case 0x32:
					$this->copyToCanvas($this->graphics_buffer->img);
				break;
			default:
					printf("\nGS ( L	 - Set up and print the symbol\n%d\n%s\n",$fn,$this->dump($d));
				break;
		}
	}

	public function doGS_2A($x, $y, $d)	{printf("\nGS * - Define downloaded bit image\n%s\n%s\n%s\n",$x,$y,$this->dump($d));}
	public function doGS_2F($m)					{printf("\nGS /	- Print downloaded bit image\n%02X\n",$m);}

	public function doGS_76($m, $x, $y, $d) {
		#printf("\nGS v 0		- Print raster bit image\n%02X		%04X %04X\n%s\n",$m,$x,$y,$this->dump($d));
		$this->bitmapXY($this->currentXY->x, $this->currentXY->y,$d,[1+($m&1),1+($m&2)]);
	}

	/* ------------------------------------------------------------------------------------------------------------------ */

	public function doGS_3A()			{ printf("\nGS :			- Start/end macro definition\n"); }
	public function doGS_5E($r, $t, $m)	{ printf("\nGS ^			- Execute macro\n%02X %02X %02X \n",$r,$t,$m); }

	/* ------------------------------------------------------------------------------------------------------------------ */
}
