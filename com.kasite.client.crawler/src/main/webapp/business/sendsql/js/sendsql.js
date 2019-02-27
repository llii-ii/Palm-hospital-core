
	$(function(){
		$('#sql').html('select * from HDSD00_69');
		$('#targetjson').html('哈哈');
	});
	
	function queryWJData(num){
		var param = {};
		param.sql=$("#sql").val();
		var d = Commonjs.ajax('../../online/sql.do',param,false);
		if(d.respCode == 10000) {
			$('#targetjson').html('');
//			$('#targetjson').html(formatJson(JSON.stringify(d.data)));
//			formatJson(JSON.stringify(d.data))
			$('#result').html(syntaxHighlight(d.data));
			return;
		}
	}
	
	function syntaxHighlight(json) {
		if (typeof json != 'string') {
		json = JSON.stringify(json, undefined, 2);
		}
		json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
		return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
		var cls = 'number';
		if (/^"/.test(match)) {
		if (/:$/.test(match)) {
		cls = 'key';
		} else {
		cls = 'string';
		}
		} else if (/true|false/.test(match)) {
		cls = 'boolean';
		} else if (/null/.test(match)) {
		cls = 'null';
		}
		return '<span class="' + cls + '">' + match + '</span>';
		});
		}
	
	function repeat(s, count) {
        return new Array(count + 1).join(s);
    }

    function formatJson(json) {
        var i           = 0,
            len          = 0,
            tab         = "    ",
            targetJson     = "",
            indentLevel = 0,
            inString    = false,
            currentChar = null;

        for (i = 0, len = json.length; i < len; i += 1) { 
            currentChar = json.charAt(i);

            switch (currentChar) {
            case '{': 
            case '[': 
                if (!inString) { 
                    targetJson += currentChar + "\n" + repeat(tab, indentLevel + 1);
                    indentLevel += 1; 
                } else { 
                    targetJson += currentChar; 
                }
                break; 
            case '}': 
            case ']': 
                if (!inString) { 
                    indentLevel -= 1; 
                    targetJson += "\n" + repeat(tab, indentLevel) + currentChar; 
                } else { 
                    targetJson += currentChar; 
                } 
                break; 
            case ',': 
                if (!inString) { 
                    targetJson += ",\n" + repeat(tab, indentLevel); 
                } else { 
                    targetJson += currentChar; 
                } 
                break; 
            case ':': 
                if (!inString) { 
                    targetJson += ": "; 
                } else { 
                    targetJson += currentChar; 
                } 
                break; 
            case ' ':
            case "\n":
            case "\t":
                if (inString) {
                    targetJson += currentChar;
                }
                break;
            case '"': 
                if (i > 0 && json.charAt(i - 1) !== '\\') {
                    inString = !inString; 
                }
                targetJson += currentChar; 
                break;
            default: 
                targetJson += currentChar; 
                break;                    
            } 
        } 
        $('#targetjson').html(targetJson);
//        document.form1.targetjson.value=targetJson;
        return;
    }
	
  	