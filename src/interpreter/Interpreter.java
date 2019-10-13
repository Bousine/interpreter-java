package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Interpreter {
	
	static Hashtable<String, String> _map;
	static Hashtable<String, List<String>> _funcmap;
	static Hashtable<String, Hashtable<String,String>> _envi;
	static String _retVal;
		
	
	
	public static ArrayList<String> fileRead(String input){
		ArrayList<String> inList = new ArrayList<String>();
		try{
			FileReader fr = new FileReader(input);
			BufferedReader br = new BufferedReader(fr);
			
			String str;
			
			while((str = br.readLine()) != null){
				inList.add(str);
			}
			br.close();
		}
		catch(IOException e){
			System.out.println("File not found!");
		}
		return inList;
	}
	
	public static void fileWrite(String output, ArrayList<String> list){
		try{
			FileWriter fw = new FileWriter(output);
			PrintWriter pw = new PrintWriter(fw);
			
			for(String s : list){		
				pw.println(s);
			}
			pw.close();
		}
		catch(IOException e){
			System.out.println("Error!");
		}
	}
	
	public static ArrayList<String> lexer(ArrayList<String> inList){
		ArrayList<String> tokenlist = new ArrayList<String>();
		boolean mustEnd = false;
		for(String line : inList){
			String token = "";
			String str = "";
			char c;
			String num = "";
			int state = 0;
			int namestate = 0;
			
			/*boolean funnamestate = false;
			boolean funargstate = false;*/
			boolean namerror = false;
			boolean numberstart = false;
			boolean notint = false;
			
			/*String funname="";
			String argname="";*/
			String name = "";
			
			for(int i = 0; i < line.length(); i++){
				token += line.charAt(i);
				c = line.charAt(i);
				//System.out.println(token);
				
				
				if(token.equals(" ")) {
					if(state == 0){
						token = "";
					}
					/*else if(state == 1){
						token = " ";
					}*/
				}
				
				else if(token.equals(":true:")){
					tokenlist.add("BOOLEAN::true:");
					token = "";
				}
				else if(token.equals(":false:")){
					tokenlist.add("BOOLEAN::false:");
					token = "";
				}
				else if(token.equals(":error:")){
					tokenlist.add(":error:");
					token = "";
				}
				else if(token.equals("push") && namestate == 0) {
					//System.out.println("Found push");
					tokenlist.add("PUSH");
					token = "";
					namestate = 1;
				}
				else if(token.equals("pop") && namestate == 0) {
					//System.out.println("Found pop");
					tokenlist.add("POP");
					token = "";
				}
				else if(token.equals("add") && namestate == 0) {
					//System.out.println("Found add");
					tokenlist.add("ADD");
					token = "";
				}
				else if(token.equals("sub") && namestate == 0) {
					//System.out.println("Found sub");
					tokenlist.add("SUB");
					token = "";
				}
				else if(token.equals("mul") && namestate == 0) {
					//System.out.println("Found mul");
					tokenlist.add("MUL");
					token = "";
				}
				else if(token.equals("div") && namestate == 0) {
					//System.out.println("Found div");
					tokenlist.add("DIV");
					token = "";
				}
				else if(token.equals("neg") && namestate == 0) {
					//System.out.println("Found neg");
					tokenlist.add("NEG");
					token = "";
				}
				else if(token.equals("rem") && namestate == 0) {
					//System.out.println("Found rem");
					tokenlist.add("REM");
					token = "";
				}
				else if(token.equals("swap") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("SWAP");
					token = "";
				}
				else if(token.equals("cat") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("CAT");
					token = "";
				}
				else if(token.equals("and") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("AND");
					token = "";
				}
				else if(token.equals("or") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("OR");
					token = "";
				}
				else if(token.equals("not") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("NOT");
					token = "";
				}
				else if(token.equals("equal") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("EQUAL");
					token = "";
				}
				else if(token.equals("lessThan") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("LESSTHAN");
					token = "";
				}
				else if(token.equals("bind") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("BIND");
					token = "";
				}
				else if(token.equals("if") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("IF");
					token = "";
				}
				else if(token.equals("let") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("LET");
					token = "";
				}
				else if(token.equals("end") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("END");
					token = "";
				}
				else if(token.equals("return") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("RETURN");
					token = "";
				}
				else if(token.equals("call") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("CALL");
					token = "";
				}
				else if(token.equals("funEnd") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("FUNEND");
					token = "";
					mustEnd = false;
				}
				
				else if(token.equals("fun") && namestate == 0 && !mustEnd){
					break;
				}
				/*else if(token.equals("fun") && namestate == 0) {
					//System.out.println("Found swap");
					tokenlist.add("FUN");
					token = "";
				}*/
				else if(token.equals("quit") && namestate == 0) {
					//System.out.println("Found quit");
					tokenlist.add("QUIT");
					token = "";
				}
				else if(token.equals("\"")){
					if(state == 0){state = 1; namestate = 0;}
					else if(state == 1){
						//System.out.println("Found a String");
						tokenlist.add("STRING:" + str + "\"");
						state = 0;
						namestate = 0;
					}
				}
				else if((c=='0' || c=='1' || c=='2' || c=='3' || c=='4' || c=='5' || c=='6' || c=='7' || c=='8' || c=='9' || c=='-' || c=='.') && notint == false){
					num += c;
					
					namestate = 0;
					if(num.equals("-0")){num = "0";}
					numberstart = true;
				}
				
				
				/*else if(funnamestate){
					name += c;
					if(c ==' '){
						funnamestate=false;
						funargstate=true;
					}
					if((c >='a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
						notint = true;
					}
					if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z'){
						namerror = true;
					}
				}
				else if(funargstate){
					name += c;
					if((c >='a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
						notint = true;
					}
					if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z'){
						namerror = true;
					}
				}*/
				
				else if(namestate == 1){
					name += c;
					if((c >='a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
						notint = true;
					}
					if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z'){
						namerror = true;
					}
				}
				if(state == 1){
					str += token;
					token = "";
				}
				if(i == line.length() - 1){
					//System.out.println(token);
					if(numberstart && !token.matches("-?\\d+")){
						num = "";
						tokenlist.add(":error:");
					}
				}

			}
			if(line.matches("fun [a-zA-Z][a-zA-Z0-9]* [a-zA-Z][a-zA-Z0-9]*")){
				String[] funelements = line.split(" ");
				tokenlist.add("FUN");
				tokenlist.add("NAME:"+funelements[1]);
				tokenlist.add("NAME:"+funelements[2]);
				mustEnd = true;
			}
			
			if(line.matches("inOutFun [a-zA-Z][a-zA-Z0-9]* [a-zA-Z][a-zA-Z0-9]*")){
				String[] funelements = line.split(" ");
				tokenlist.add("IOFUN");
				tokenlist.add("NAME:"+funelements[1]);
				tokenlist.add("NAME:"+funelements[2]);
				mustEnd = true;
			}
			
			if(num.contains(".")){
				
				num = "";
				tokenlist.add(":error:");
			}
			if(!num.matches("-?\\d+") && !num.isEmpty()){
				num = "";
				tokenlist.add(":error:");
			}
			if(namerror){
				name = "";
				tokenlist.add(":error:");
			}
		
			namestate = 0;
			
			if(!name.isEmpty()){tokenlist.add("NAME:" + name);}
			else if(!num.isEmpty()){tokenlist.add("INT:" + num);}
			
		}
		//System.out.println(tokenlist);
		return tokenlist;
	}
	
	public static void binOp(Stack<String> stack, String type){
		if(stack.size() >= 2){
			String operand1 = stack.pop();
			String operand2 = stack.pop();
			
			String temp1 = operand1;
			String temp2 = operand2;
			if(_map == null){_map = new Hashtable<String,String>();}
			//if(type.equals("MUL"))System.out.println(_map);
			
			if(operand1.substring(0, 4).equals("NAME") && _map.get(operand1) != null){operand1 = _map.get(operand1);}
			if(operand2.substring(0, 4).equals("NAME") && _map.get(operand2) != null){operand2 = _map.get(operand2);}
			if(operand1.substring(0, 3).equals("INT") && operand2.substring(0, 3).equals("INT")){
				int op1 = Integer.parseInt(operand1.substring(4));
				int op2 = Integer.parseInt(operand2.substring(4));
				int result = 0;
				if(type.equals("ADD")){
					result = op1 + op2;
				}
				else if(type.equals("SUB")){
					result = op2 - op1;
				}
				else if(type.equals("MUL")){
					result = op2 * op1;
				}
				else if(type.equals("DIV") && op1 != 0){
					result = op2 / op1;
				}
				else if(type.equals("REM") && op1 != 0){
					result = op2 % op1;
				}
				
				if((type.equals("DIV") || type.equals("REM")) && op1 == 0){
					stack.push(temp2);
					stack.push(temp1);
					stack.push(":error:");
				}else{
					stack.push("INT:"+result);
				}
			}
			else{
				stack.push(temp2);
				stack.push(temp1);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void compare(Stack<String> stack, String type){
		if(stack.size() >= 2){
			String operand1 = stack.pop();
			String operand2 = stack.pop();
			String temp1 = operand1;
			String temp2 = operand2;
			if(operand1.substring(0, 4).equals("NAME") && _map.get(operand1) != null){operand1 = _map.get(operand1);}
			if(operand2.substring(0, 4).equals("NAME") && _map.get(operand2) != null){operand2 = _map.get(operand2);}
			if(operand1.substring(0, 3).equals("INT") && operand2.substring(0, 3).equals("INT")){
				int op1 = Integer.parseInt(operand1.substring(4));
				int op2 = Integer.parseInt(operand2.substring(4));
				boolean result = false;
				if(type.equals("EQUAL")){
					result = (op2 == op1);
				}
				else if(type.equals("LESSTHAN")){
					result = op2 < op1;
				}
				stack.push("BOOLEAN::"+result+":");
				
			}
			else{
				stack.push(temp2);
				stack.push(temp1);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void logicalBinOp(Stack<String> stack, String type){
		if(stack.size() >= 2){
			String operand1 = stack.pop();
			String operand2 = stack.pop();
			String temp1 = operand1;
			String temp2 = operand2;
			if(operand1.substring(0, 4).equals("NAME") && _map.get(operand1) != null){operand1 = _map.get(operand1);}
			if(operand2.substring(0, 4).equals("NAME") && _map.get(operand2) != null){operand2 = _map.get(operand2);}
			/*System.out.println(operand1);
			System.out.println(operand2);*/
			if(operand1.length()>8 && operand2.length()>8 && operand1.substring(0, 7).equals("BOOLEAN") && operand2.substring(0, 7).equals("BOOLEAN")){
				boolean op1 = Boolean.parseBoolean(operand1.substring(9, operand1.length()-1));
				boolean op2 = Boolean.parseBoolean(operand2.substring(9, operand2.length()-1));
				boolean result = false;
				if(type.equals("AND")){
					result = op1 && op2;
				}
				else if(type.equals("OR")){
					result = op2 || op1;
				}
				
				stack.push("BOOLEAN::"+result+":");
				
			}
			else{
				stack.push(temp2);
				stack.push(temp1);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void cat(Stack<String> stack){
		if(stack.size() >= 2){
			String operand1 = stack.pop();
			String operand2 = stack.pop();
			String temp1 = operand1;
			String temp2 = operand2;
			if(operand1.substring(0, 4).equals("NAME") && _map.get(operand1) != null){operand1 = _map.get(operand1);}
			if(operand2.substring(0, 4).equals("NAME") && _map.get(operand2) != null){operand2 = _map.get(operand2);}
			if(operand1.length()>7 && operand2.length()>7 && operand1.substring(0, 6).equals("STRING") && operand2.substring(0, 6).equals("STRING")){
				String op1 = (operand1.substring(8));
				String op2 = (operand2.substring(7, operand2.length()-1));
				String result = "";
				result = op2 + op1;
				//System.out.println(result);
				stack.push("STRING:"+result);
				//System.out.println(stack.peek());
			}
			else{
				stack.push(temp2);
				stack.push(temp1);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void unOp(Stack<String> stack, String type){
		if(stack.size() >= 1){
			String operand = stack.pop();
			String temp = operand;
			if(operand.substring(0, 4).equals("NAME") && _map.get(operand) != null){operand = _map.get(operand);}
			
			if(operand.substring(0, 3).equals("INT") && type.equals("NEG")){
				int op = Integer.parseInt(operand.substring(4));
				int result = 0;
				if(type.equals("NEG")){
					result = 0 - op;
				}	
				stack.push("INT:"+result);
			}
			else if(operand.length() > 8 && operand.substring(0, 7).equals("BOOLEAN") && type.equals("NOT")){
				boolean op = Boolean.parseBoolean(operand.substring(9, operand.length()-1));
				boolean result = false;
				if(type.equals("NOT")){
					result = !op;
				}
				stack.push("BOOLEAN::"+result+":");
			}
			else{
				stack.push(temp);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void swap(Stack<String> stack){
		if(stack.size() >= 2){
			String operand1 = stack.pop();
			String operand2 = stack.pop();
			stack.push(operand1);
			stack.push(operand2);
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void bind(Stack<String> stack, Hashtable<String, String> map){
		if(stack.size() >= 2){
			String value = stack.pop();
			String temp = value;
			String name = stack.pop();
			if(value.substring(0, 4).equals("NAME") && _map.get(value) != null){value = _map.get(value);}
			if(value.substring(0, 4).equals("NAME") && _map.get(value) == null){value = ":error:";}
			if(name.substring(0, 4).equals("NAME") && (value.substring(0, 3).equals("INT") || value.equals(":unit:") || value.substring(0, 6).equals("STRING") || value.substring(0, 7).equals("BOOLEAN")  )){
				stack.push(":unit:");
				map.put(name, value);
			}
			else{
				stack.push(name);
				stack.push(temp);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
		//System.out.println(_map);
	}
	
	public static void conif(Stack<String> stack){
		if(stack.size() >= 3){
			String x = stack.pop();
			String y = stack.pop();
			String z = stack.pop();
			String temp1 = x;
			String temp2 = y;
			String tempz = z;
			if(z.substring(0, 4).equals("NAME") && _map.get(z) != null){z = _map.get(z);}
			if(z.length()>7 && z.substring(0, 7).equals("BOOLEAN")){
				if(z.equals("BOOLEAN::true:")){
					stack.push(y);
				}
				else{
					stack.push(x);
				}
			}
			else{
				stack.push(z);
				stack.push(y);
				stack.push(x);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	public static void call(Stack<String> stack, String output, boolean inOut){
		if(stack.size() >= 2){
			String arg = stack.pop();
			String funName = stack.pop();
			String tempfunName = funName;
			String temparg = arg;
			List<String> functokens = _funcmap.get(funName);
			Hashtable<String,String> tempmap = _map;
			_map = _envi.get(tempfunName);
			//System.out.println(functokens);
			ArrayList<String> tokenlist = new ArrayList<String>();
			if(functokens != null) {tokenlist = new ArrayList<String>(functokens);}
			else{functokens=null;}
			//System.out.println(temparg);
			if(arg.substring(0, 4).equals("NAME")){arg = tempmap.get(arg);}
			if(functokens != null && arg != null && !temparg.equals(":error:")){
				
				String funparam = functokens.get(0);
				//System.out.println(funparam);
				_map.put(funparam, arg);
				
				//System.out.println(_map.get(funparam));
				parse(tokenlist, output);
				if(inOut){_map.put(temparg, _map.get(funparam));}
				//System.out.println(_map);
				//System.out.println(tempmap);
				if(inOut){
					for(Map.Entry<String, String> entry : _map.entrySet()){
						String key = entry.getKey();
						if(tempmap.containsKey(key)){
							tempmap.replace(key, entry.getValue());
						}
					}
				}
				_map = tempmap;
			}
			else{
				stack.push(tempfunName);
				stack.push(temparg);
				stack.push(":error:");
			}
		}
		else{
			stack.push(":error:");
		}
	}
	
	
	public static void removeID(ArrayList<String> list){
		for(int i = 0; i < list.size(); i++){
			String current = list.get(i);
			//System.out.println(list);
			if(current.contains("INT")){
				current = current.substring(4);
				list.set(i, current);
			}
			else if(current.contains("STRING")){
				current = current.substring(8, current.length()-1);
				list.set(i, current);
			}
			else if(current.contains("BOOLEAN")){
				current = current.substring(8);
				list.set(i, current);
			}
			else if(current.contains("NAME")){
				current = current.substring(5);
				list.set(i, current);
			}
		}
	}
	
	
	public static void parse(ArrayList<String> tokenlist, String output){
		
		
		int tokenNo = 0;
		String funName = "";
		String funArg = "";
		boolean notACall = false;
		boolean inOut = false;
		
		Stack<String> stack = new Stack<String>();
		Stack<Stack<String>> frames = new Stack<Stack<String>>();
		Stack<Hashtable<String,String>> mapstack = new Stack<Hashtable<String,String>>();
		
		for(int i = 0; i < tokenlist.size(); i++){
			
			String current = tokenlist.get(i);
			
			if(current == "PUSH"){
				stack.push(tokenlist.get(i+1));
			}
			else if(current == "POP"){
				if(!stack.isEmpty()){stack.pop();}
				else{stack.push(":error:");}
			}
			else if(current == "ADD"){
				binOp(stack, "ADD");
			}
			else if(current == "SUB"){
				binOp(stack, "SUB");
			}
			else if(current == "MUL"){
				binOp(stack, "MUL");
			}
			else if(current == "DIV"){
				binOp(stack, "DIV");
			}
			else if(current == "REM"){
				binOp(stack, "REM");
			}
			else if(current == "NEG"){
				unOp(stack, "NEG");
			}
			else if(current == "SWAP"){
				swap(stack);
			}
			else if(current == "CAT"){
				cat(stack);
			}
			else if(current == "AND"){
				logicalBinOp(stack, "AND");
			}
			else if(current == "OR"){
				logicalBinOp(stack, "OR");
			}
			else if(current == "NOT"){
				unOp(stack, "NOT");
			}
			else if(current == "EQUAL"){
				compare(stack, "EQUAL");
			}
			else if(current == "LESSTHAN"){
				compare(stack, "LESSTHAN");
			}
			else if(current == "BIND"){
				bind(stack, _map);
			}
			else if(current == "IF"){
				conif(stack);
			}
			else if(current == "FUN"){
				stack.push(":unit:");
				tokenNo = i+2;
				funName = tokenlist.get(i+1);
				funArg  = tokenlist.get(i+2);
				notACall = true;
				
				Stack<String> temp = stack;
				Hashtable<String, String> tempmap = _map;
				frames.push(temp);
				mapstack.push(tempmap);
				stack = new Stack<String>();
				_map = new Hashtable<String,String>();
				_map.putAll(tempmap);
				
			}
			else if(current == "IOFUN"){
				stack.push(":unit:");
				tokenNo = i+2;
				funName = tokenlist.get(i+1);
				funArg  = tokenlist.get(i+2);
				notACall = true;
				
				Stack<String> temp = stack;
				Hashtable<String, String> tempmap = _map;
				frames.push(temp);
				mapstack.push(tempmap);
				stack = new Stack<String>();
				_map = new Hashtable<String,String>();
				_map.putAll(tempmap);
				inOut = true;
			}
			else if(current == "FUNEND"){
				List<String> l = tokenlist.subList(tokenNo, i);
				_funcmap.put(funName, l);
				Stack<String> temp = frames.pop();
				Hashtable<String,String> tempmap = mapstack.pop();
				Hashtable<String,String> copy = _map;
				_envi.put(funName, copy);
				stack = temp;
				_map = tempmap;
				/*if(inOut){
					for(Map.Entry<String, String> entry : _map.entrySet()){
						String key = entry.getKey();
						if(copy.containsKey(key)){
							_map.replace(key, entry.getValue());
						}
					}
				}*/
			}
			else if(current == "CALL"){
				call(stack, output, inOut);
				if(_retVal!=null){stack.push(_retVal);}
			}
			else if(current == "RETURN" && !notACall){
				_retVal = stack.peek();
				
				if(_retVal.length()>3 &&_retVal.substring(0,4).equals("NAME")){
					//System.out.println(_map.get(_retVal));
					_retVal = _map.get(_retVal);
				}
			}
			else if(current == "LET"){
				Stack<String> temp = stack;
				Hashtable<String, String> tempmap = _map;
				frames.push(temp);
				mapstack.push(tempmap);
				stack = new Stack<String>();
				_map = new Hashtable<String,String>();
				_map.putAll(tempmap);
			}
			else if(current == "END"){
				//System.out.println(frames);
				Stack<String> temp = frames.pop();
				Hashtable<String,String> tempmap = mapstack.pop();
				temp.push(stack.peek());
				//System.out.println(temp);
				stack = temp;
				_map = tempmap;
			}
			else if(current == "QUIT"){
				ArrayList<String> endStack = new ArrayList<String>();
				while(!stack.isEmpty()){
					endStack.add(stack.pop());
				}
				removeID(endStack);
				fileWrite(output, endStack);
			}
		}
		//System.out.println(_funcmap.get("NAME:method"));
	}
	
	public static void interpreter(String input, String output){
		_map = new Hashtable<String, String>();
		_funcmap = new Hashtable<String, List<String>>();
		_envi = new Hashtable<String, Hashtable<String,String>>();
		ArrayList<String> inList = new ArrayList<String>();
		inList = fileRead(input);
		
		ArrayList<String> tokenlist = lexer(inList);
		parse(tokenlist, output);
		
		
		//fileWrite(output, inList);
		
		//System.out.println(inList);
		
		//System.out.println(tokenlist);
		
	}
	
	public static void main(String[] args){
		interpreter("input6.txt", "testout.txt");
	}

}
