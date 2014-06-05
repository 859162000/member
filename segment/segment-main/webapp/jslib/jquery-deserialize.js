/**
 * This function sets the values of form element variables from an array
 * This is the reverse process of Mark Constable's serialize function
 * It is expected to be used as a call back for an ajax call that retrieves the form data
 * @param data : array or hash containing name,value pairs for elements in the form
 *
 * Examples
 *
 * 1. Deserialize from an array
 *    $('#form-id').deserialize([{'name':'firstname','value':'John'},{'name':'lastname','value':'Resig'}]);
 *
 * 2. Deserialize from a hash(object)
 *    $('#form-id').deserialize({'firstname':'John','lastname':'Resig'});
 *
 * 3. Deserialize multiple config for select/radio/checkbox
 *    $('#form-id').deserialize({'toppings':['capsicum','mushroom','extra_cheese'],'size':'medium'})
 * which will set the corresponding select/radio/checkbox config for toppings
 *
 * 3. Deserialize multiple config for select/radio/checkbox and with isPHPnaming = true
 *    $('#form-id').deserialize({'toppings':['capsicum','mushroom','extra_cheese'],'size':'medium'},{isPHPnaming:true})
 * which will set the corresponding select/radio/checkbox config for toppings, but will ignore select names ending with []
 * 
 * 4. Deserialize multiple config for select/radio/checkbox and with inputPrefix = "MyPrefix"
 *    $('#form-id').deserialize({'toppings':['capsicum','mushroom','extra_cheese'],'size':'medium'},{inputPrefix:'MyPrefix'})
 * which will set the corresponding select/radio/checkbox config for toppings, but suppose every input tag name attribute has prefix 'MyPrefix'
 *
 * @return         the jQuery Object
 * @author         Ashutosh Bijoor (bijoor@reach1to1.com)
 * @version        0.35
 */
$.fn.deserialize = function(d,config) {
	var data = d;
	var me = this;

	if (d == undefined || d == null) {
		return me;
	}

	config = $.extend({ inputPrefix: null,
						isPHPnaming: false,
						overwrite: true}, config);
	
	// check if data is an array, and convert to hash, converting multiple entries of 
	// same name to an array
	if (d.constructor == Array)	{
		data={};
		for(var i=0; i<d.length; i++) {
			if (typeof data[d[i].name] != 'undefined') {
				if (data[d[i].name].constructor != Array) {
					data[d[i].name]=[data[d[i].name], d[i].value];
				} else {
					data[d[i].name].push(d[i].value);
				}
			} else {
				data[d[i].name] = d[i].value;
			}
		}
	}
	
	/**
	 * Convert the value into type string.
	 * If value is Array, each value in array will be converted into type string.
	 * @param value
	 * @return
	 */
	function convertToString(value) {
		if(value.constructor == Array) {
			var strValues = new Array(value.length);
			for( i=0;i<value.length;i++) {
				var v = value[i];
				if(typeof v != 'string') {
					strValues[i] = v.toString();
				}
				else {
					strValues[i] = v;
				}
			}
			return strValues;
		}
		else {
			var strValue;
			if(typeof value != 'string') {
				strValue = value.toString();
			}
			else {
				strValue = value;
			}
			return strValue;
		}
	}
		

	// now data is a hash. insert each parameter into the form
	$('input,select,textarea',me)
	.each(function() {
			  var p = this.name;
			  var v = [];
			  
			  // handle wierd PHP names if required
			  if(config.isPHPnaming) {
				  p = p.replace(/\[\]$/,'');
			  }
			  if(config.inputPrefix != null && config.inputPrefix.length > 0) {
				  if(p.indexOf(config.inputPrefix) == 0) {
					  p = p.substring(config.inputPrefix.length);
				  }
			  }
			  if(p && data[p] != undefined) {
				  v = (data[p].constructor == Array) ? data[p] : [data[p]];
			  }
			  // Additional parameter overwrite
			  if(config.overwrite == true || data[p]) {
				  switch(this.type || this.tagName.toLowerCase()) {
				  case "radio":
				  case "checkbox":
					  this.checked=false;
					  for(var i=0;i<v.length;i++) {
						  var strValue = convertToString(v[i]);
						  this.checked|=(this.value!='' && strValue==this.value);
					  }
					  break;			  
				  case "select-multiple":
				  case "select-one":
				  case "select":
					  for( i=0;i<this.options.length;i++) {
						  this.options[i].selected=false;
						  for(var j=0;j<v.length;j++) {
							  var strValue = convertToString(v[j]);
							  this.options[i].selected|=(this.options[i].value!='' && this.options[i].value==strValue);
						  }
					  }
					  break;
				  case "button":
				  case "submit":
					  var strValues = convertToString(v);
					  this.value = (strValues.length>0) ? strValues.join(',') : this.value;
					  break;
				  default:
					  var strValues = convertToString(v);
					  this.value=strValues.join(',');
				  }
			  }
		  });
	return me;
};
