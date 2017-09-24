<html>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>Big Factorial</title>
   <link rel="stylesheet" type="text/css" href="css/zeek.css" />
   <script>

   </script>
	<body style="text-align: left;">
<a href="#" target="_blank">open separately?</a>   CHECK OUT <a href="template.jsp">HOW-I-MADE-THIS</a>
<hr>   
Enter a number &nbsp;&nbsp;<form action="./bigfactorial.jsp" method="get"><input type="text" length="50" name="input"/>&nbsp;&nbsp;&nbsp;<input type="submit" value="GO"/></form>
<hr>
<div id="output">
<%= if (null != request.getParameter("input")){
	
        long outSum = 0;
        String outAns = "";
        String outTime = "";""

        long start = System.currentTimeMillis();
		int[] answerArray = new int[100000];
		answerArray[0] = 1;

		int maxAnswerDigit = 1;
		int FACTORIAL_OF =0;
		try {
		    FACTORIAL_OF = Integer.parseInt(request.getParameter("input"));
		}catch(Exception e){
			//do nothing, fuck user!
		}
		for (int n = 1; n <= FACTORIAL_OF; n++) {
			for (int digit = 0; digit < maxAnswerDigit; digit++)
				answerArray[digit] = answerArray[digit] * n;

			for (int digit = 0; digit < maxAnswerDigit; digit++) {
				if (answerArray[digit] > 9) {
					answerArray[digit + 1] += answerArray[digit] / 10;
					answerArray[digit] = answerArray[digit] % 10;
					if (digit + 1 == maxAnswerDigit)
						maxAnswerDigit++;

				}
			}
		}
		for (int i = maxAnswerDigit - 1; i >= 0; i--) {
			//System.out.print(answerArray[i]);
			outAns +=answerArray[i];
			sum += answerArray[i];
		}
		//System.out.println("\nSum=" + sum);
		//System.out.println("answered in "+ (System.currentTimeMillis() - start) / 1000.0 + " sec.");
		outSum = sum;
		outTime = ""+(System.currentTimeMillis() - start) / 1000.0)+" seconds";
}
%>
Total Time Taken = <%=outTime %><br>
Sum of all the digits = <%=outSum %><br>
ANSWER <br> <%=outAns %><<hr>
</div>
</body>
</html>
