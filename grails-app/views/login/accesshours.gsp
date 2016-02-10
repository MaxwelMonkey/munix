<script>
setTimeout("logout()",100);
alert("You don't have access to the system during these hours. Please try again later.");


function logout(){
	window.location.href="${createLink(controller:'logout')}";
}
</script>