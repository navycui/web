(function($){$.fn.imgMark=function(options){options=options||{};var defaultOpt={checkboxCls:options.checkboxCls||'img_checkbox',radioCls:options.radioCls||'img_radio',checkedCls:options.checkedCls||'img_checked',selectedCls:options.selectedCls||'img_selected',hideCls:'img_hide'};return this.each(function(){var $this=$(this);var wrapTag=$this.attr('type')=='checkbox'?'<div class="'+defaultOpt.checkboxCls+'">':'<div class="'+defaultOpt.radioCls+'">';if($this.attr('type')=='checkbox'){$this.addClass(defaultOpt.hideCls).wrap(wrapTag).change(function(){if($(this).is(':checked')){$(this).parent().addClass(defaultOpt.checkedCls);}
else{$(this).parent().removeClass(defaultOpt.checkedCls);}});if($this.is(':checked')){$this.parent().addClass(defaultOpt.checkedCls);}}
else if($this.attr('type')=='radio'){$this.addClass(defaultOpt.hideCls).wrap(wrapTag).change(function(){$('input[name="'+$(this).attr('name')+'"]').each(function(){if($(this).is(':checked')){$(this).parent().addClass(defaultOpt.selectedCls);}else{$(this).parent().removeClass(defaultOpt.selectedCls);}});});if($this.is(':checked')){$this.parent().addClass(defaultOpt.selectedCls);}}});}})(jQuery);