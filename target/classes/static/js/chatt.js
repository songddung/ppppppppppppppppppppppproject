	function getId(id){
		return document.getElementById(id);
	}

	var data = {};//전송 데이터(JSON)

	var ws ;
	var mid = getId('mid');
	var btnLogin = getId('btnLogin');
	var btnSend = getId('btnSend');
	var talk = getId('talk');
	var msg = getId('msg');
	var loginClicked = false;

	btnLogin.onclick = function(){
		if (!loginClicked) {
		ws = new WebSocket("ws://" + location.host + "/chatt");

		loginClicked = true;
        btnLogin.disabled = ture;
		}
		ws.onmessage = function(msg){
			var data = JSON.parse(msg.data);
			var css;
			
			if(data.mid == mid.value){
				css = 'class=me';
			}else{
				css = 'class=other';
			}
			
			var item = `<div ${css} >
							<span><b>${data.mid}</b></span> [ ${data.date} ]<br/>
						<span>${data.msg}</span>
							</div>`;
						
			talk.innerHTML += item;
			talk.scrollTop=talk.scrollHeight;//스크롤바 하단으로 이동
		}
	}

	msg.onkeyup = function(ev){
		if(ev.keyCode == 13){
			send();
		}
	}

	btnSend.onclick = function(){
		send();
	}

	function send(){
		if(msg.value.trim() != ''){
			data.mid = getId('mid').value;
			data.msg = msg.value;
			data.date = new Date().toLocaleString();
			var temp = JSON.stringify(data);
			ws.send(temp);
		}
		msg.value ='';
		
	}

	document.addEventListener('DOMContentLoaded', function () {
		const emojiButtons = document.querySelectorAll('.emojiButton');
		const msgInput = document.getElementById('msg');
	
		emojiButtons.forEach(function (button) {
		button.addEventListener('click', function () {
			const emoji = button.textContent;
			msgInput.value += emoji;
		});
		});
	});