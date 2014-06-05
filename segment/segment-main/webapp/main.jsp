<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Pathlet E-Shop Main Page</title>
<jsp:include page="header.jsp"><jsp:param name="groups" value="jquery,jquery-ui,layout,ztree,common"/></jsp:include>
<style type="text/css">
	a.topLink:link,a.topLink:visited{
		color:#b0dbee;
		text-decoration:none;
	}
	
	a.topLink:hover,a.topLink:active{
		color:#f68000;
		text-decoration:none;
	}
	
	/************************/
	/* Layout styles        */
	/************************/
	.ui-layout-center{
	/*	NOTE: hiding an iframe may cause JS errors if the iframe page autoruns a script, so...
		onopen: loadIframePage() == loads the *real* iframe page from "longdesc" attribute at 1st open */
		display:	none;
		overflow:	hidden;
		padding:	0;		/* west pane has a scrolling content-div, so remove padding */
	}
	
	iframe {
		padding:	0; /* iframes should not have padding */
		overflow:	auto;
	}
	
	/* color panes so they can be seen */
	.ui-layout-pane {
		color:			#000;
		background:		#EEE;
	}
	/* masks are usually transparent - make them visible (must 'override' default) */
	.ui-layout-mask {
		background:	#C00;
		opacity:	.20;
		filter:		alpha(opacity=20);
	}


	/************************/
	/*Layout splitter style */
	/************************/
	.ui-layout-toggler-west-open {
		background-image: url(images/switcha.gif);
	}
	
	.ui-layout-toggler-west-open:hover {
		background-image: url(images/switcha.gif);
	}
	
	.ui-layout-toggler-west-closed {
		background-image: url(images/switchb.gif);
	}
	
	.ui-layout-toggler-west-closed:hover {
		background-image: url(images/switchb.gif);
	}
	
	
	.ui-layout-toggler-north-open {
		background-image: url(images/switchc.gif);
	}
	
	.ui-layout-toggler-north-open:hover {
		background-image: url(images/switchc.gif);
	}
	
	.ui-layout-toggler-north-closed {
		background-image: url(images/switchd.gif);
	}
	
	.ui-layout-toggler-north-closed:hover {
		background-image: url(images/switchd.gif);
	}

	
</style>

<script type="text/javascript">
var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

var startPage = 'start.html';
var menuURL = 'menu.js';

$(document).ready(function() {
	
	var setting = {
			async: {
				enable: true,
				url:menuURL
			},
			callback: {
				onClick: function(event, treeId, treeNode, clickFlag) {
					$('#centerIframe').attr('src', treeNode.href);
					return false;
				}
			}
		};

	$.fn.zTree.init($("#west-treemenu"), setting);
	$('#centerIframe').attr('src', startPage);

	myLayout = $('body').layout({
		north__slidable:		false,
		north__resizable:		false,
		north__size: 70,
		west__size:				200,
		west__slidable:		false,
		west__resizable:		false,
		togglerLength_open: 37,
		togglerLength_closed: 37,
		//onopen_end:			loadIframePage, 

		//north__spacing_open:	0,
		//north__spacing_closed:	0,
        west__onresize: function (pane, pane2) {
            $("#leftPaneMenu").height(pane2.innerHeight());
		}
	});

	//reset the left menu pane size
	$("#leftPaneMenu").height($("#LeftPane").height());
	


});
</script>
</head>
<body>
<div id="TopPane" class="ui-layout-north ui-widget" >
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td width="406" background="images/main-banner-1f.jpg"></td>
      <td width="500" background="images/main-banner-1f.jpg"></td>
      <td width="55" background="images/main-banner-1f.jpg"></td>
	  
      <td width="390" background="images/main-banner-1f.jpg"><table width="100%" height="70" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="387" align="right"><table width="387" border="0" align="right" cellpadding="0" cellspacing="0">
            <tr>
              <td width="25%" border="0" >&nbsp;</td>
              <td width="25%" border="0" >&nbsp;</td>
              <td width="25%" border="0" ><a class="topLink" target="_blank" href="http://code.google.com/p/pathlet/">Home</a></td>
              <td width="25%" border="0"><a id="logoutLink" class="topLink" href="#">Log Out</a></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td id="currentLoginInfo" height="30" align="right" nowrap="nowrap" style="color:#FFFFFF"></td>
        </tr>
      </table></td>
      <td width="44"><img src="images/main-banner-1f.jpg" width="44" height="70" /></td>
    </tr>
  </table>
</div>

<!-- #LeftPane -->
<div id="LeftPane" class="ui-layout-west ui-widget ui-widget-content">
	<div id="leftPaneMenu" style="background-color: #F5F5F5; width:100%; overflow:auto;">
        <ul id="west-treemenu" class="ztree"></ul>
        </ul>
	</div>
</div>

<!-- #RightPane --> 
<div id="RightPane" class="ui-layout-center ui-widget-content" align="center" style="overflow:auto">
	<!-- IFRAME in layout-pane -->
	<iframe id="centerIframe" src="about:blank" 
	longdesc="" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>
</div>
</body>
</html>