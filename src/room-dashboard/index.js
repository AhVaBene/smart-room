var btn = document.querySelector(".control")
var btn2 = document.querySelector(".interrupt")
var slider = document.querySelector("input[type=slider]")
var radios = document.querySelector("input[type=radio])

btn.addEventListener("click", e=>{
	e.preventDefault()
	axios.post("/control", {control:true})
	.then(res=>{console.log("control")})
})

btn2.addEventListener("click", e=>{
	e.preventDefault()
	axios.post("/control", {control:false})
	.then(res=>{console.log("control")})
})

slider.addEventListener("change", e=>{
	axios.post("/setAlpha", {alpha:slider.value})
})

radios.forEach(radio=>{
	radio.addEventListener("click", e=>{
		axios.post("/setLight", {light:radio.value})
	})
})