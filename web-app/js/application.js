var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}

jQuery(document).ready(function(){
	var form = jQuery('input[type=submit]').parents('form:first');
	form.submit(function(){
		loadProcessing();
	});
	jQuery('#processingCover').hide();

	jQuery('#loadingPopup').hide();
});

function loadProcessing(){
	jQuery('input[type=submit]').blur();
	
	jQuery('#processingCover').css("height",jQuery(document).height());

	var dialog = jQuery('#loadingPopup');
	var width = (jQuery(window).width() - dialog.width()) / 2;
	var height = (jQuery(window).height() - dialog.height()) / 2;
	dialog.css("left",width);
	dialog.css("top",jQuery(window).scrollTop()+height);

	jQuery('#processingCover').show();

	jQuery('#loadingPopup').show();

}


