package bai1;

import java.util.ArrayList;
import java.util.Scanner;
// Java Implemention of Shunting Yard Algorithm

import java.lang.Character;

public class pheptinh {

	// Method is used to get the precedence of operators
    // Check xem là toán tử hay toán hạng
    private static boolean letterOrDigit(char c)
    {
        if (Character.isLetterOrDigit(c))
            return true; // 被演算子 - toán hạng
        else
            return false; // 作用素 - toán tử
    }
 
    // Operator having higher precedence
    // value will be returned
    // Trả về cấp độ ưu tiên của toán tử -1, 1, 2, 3
    static int getPrecedence(char ch)
    {
        if (ch == '+' || ch == '-')
            return 1;
        else if (ch == '*' || ch == '/')
            return 2;
        else if (ch == '^')
            return 3;
        else
            return -1;
    }
    
    // Trả về kết quả phép tính cuối trong output queue trước khi đẩy toán tử từ operator stack sang.
    // numberRight : toán hạng cuối cùng trong output queue
    // numberLeft : toán hạng gần cuối cùng trong output queue
    // operationS : toán tử cuối cùng trong operator stack
    // VD : operator stack: +/(-	output queue: 5 2 3 8 => tính kết quả của phép tính: 3 - 8 // return -5
    static float resultList(float numberRight, float numberLeft, String operationS) {
    	float fResult = 0 ;
		
		if(operationS.equals("^"))
		{
			fResult = (float) Math.pow(numberLeft, numberRight);
		}
		if (operationS.equals("*"))
		{
			fResult = numberLeft * numberRight;
		}
		else if (operationS.equals("/"))
		{
			fResult = numberLeft / numberRight;
		}
		else if (operationS.equals("+"))
		{
			fResult = numberLeft + numberRight;
		}
		else if (operationS.equals("-"))
		{
			fResult = numberLeft - numberRight;
		}
		
		return fResult;
    }
    
    // Xóa 2 toán hạng cuối trong output queue (do đã tính kết quả của 2 toán hạng này)
    // Chèn kết quả của phép tính trên vào output queue
    // VD : operator stack: +/(-	output queue: 5 2 3 8 => operator stack: +/	output queue: 5 2 -5
    static ArrayList<Float> newlistOutput(ArrayList<Float> listOutput, char operationS) {
    	float temp = resultList(listOutput.get(listOutput.size()-1), listOutput.get(listOutput.size()-2),String.valueOf(operationS));
    	listOutput.remove(listOutput.size()-1);
    	listOutput.set(listOutput.size()-1, temp);
    	return listOutput;
    }
 
    // Method converts  given infixto postfix expression
    // to illustrate shunting yard algorithm
    // Chuyển biểu thức trung tố sang hậu tố
    static String infixToRpn(String expression)
    {
        // Initalising an empty listStack
        // List này chứa toán tử // thay thế stack
        ArrayList<Character> listStack = new ArrayList<Character>();
        
        // Initially empty string taken
        // chỗ nhớ tạm giá trị của toán hạng trước khi đẩy vào listOutput
        // VD: khi duyệt số 25 + 3 => duyệt số 2 // number = 2, tiếp theo duyệt số 5 // number = 25
        // nếu không có chỗ nhớ tạm này, mà trực tiếp đẩy toán hạng vào listOutput
        // thì kết quả trong listOutput sẽ là 2 và 5, chứ không phải 25
        String number = new String("");
        
        // Initalising an empty listOutput (for output)
        // list này chứa toán hạng
        ArrayList<Float> listOutput = new ArrayList<Float>();
 
        // Duyệt từng phần tử của chuỗi đã nhập
        for (int i = 0; i < expression.length(); ++i) {
            // Finding character at 'i'th index
            // c là ký tự thứ i của chuỗi
            char c = expression.charAt(i);
            
            // Nếu gặp ký tự không phải toán hạng và number không rỗng
            if (!letterOrDigit(c) && !number.equals("")) {
            	listOutput.add(Float.parseFloat(number)); // Chèn number vào listOutput
            	number = ""; // xóa giá trị của number - do đã chèn vào listOutput rồi
            }
            
            // If the scanned Token is an
            // operand, add it to output
            // nếu ký tự là toán hạng
            if (letterOrDigit(c)) {
            	number += c; // thêm ký tự vào number
            	if(i == expression.length()-1 && number != "") { // nếu ký tự đang duyệt là ký tự cuối cùng của chuỗi đã nhập và number chứa toán hạng
                	listOutput.add(Float.parseFloat(number)); // thì đẩy nốt toán hạng này vào listOutput
            	}
            }
 
            // If the scanned Token is an '(' 
            // push it to the stack 
            else if (c == '(') { // nếu gặp ký tự '('
            	listStack.add(c); // đẩy ký tự '(' vào listStack
            }
                
            // If the scanned Token is an ')' pop and append
            // it to output from the stack until an '(' is
            // encountered
            else if (c == ')') { // nếu gặp ký tự ')'
                while(listStack.size()>0 && listStack.get(listStack.size()-1) != '(') { // nếu listStack không rỗng và ký tự cuối trong listStack không phải '('
                	listOutput = newlistOutput(listOutput, listStack.get(listStack.size()-1)); // thì gọi newlistOutput function
                	listStack.remove(listStack.size()-1); // remove operation - xóa toán tử cuối cùng trong listStack
                }
                listStack.remove(listStack.size()-1); // remove "(" 
            }
 
            // If an operator is encountered then taken the
            // further action based on the precedence of the
            // operator
            else {
                // Nếu listStack không rỗng và toán tử đang duyệt có độ ưu tiên thấp hơn hoặc bằng toán tử cuối cùng trong listStack
                while(listStack.size()>0 && getPrecedence(c) <= getPrecedence(listStack.get(listStack.size()-1))) {
                	// gọi newlistOutput function
                	listOutput = newlistOutput(listOutput, listStack.get(listStack.size()-1));
                	listStack.remove(listStack.size()-1); // remove operation - xóa toán tử cuối cùng trong listStack
                }
                listStack.add(c); // push c to listStack - thêm toán tử đang duyệt vào listStack
            }
        }
 
        // pop all the remaining operators from
        // the stack and append them to output
        while(listStack.size()>0) { // chạy cho tới khi listStack rỗng
        	if(listStack.get(listStack.size()-1) == '(') { // kiểm tra nếu toán tử cuối của listStack là '(' -- nghĩa là không có đóng ngoặc -- chuỗi nhập vào sai
        		return "This expression is invalid"; // thì báo lỗi
        	}
            // còn nếu không có lỗi thì cứ gọi hàm tính newlistOutput function
        	listOutput = newlistOutput(listOutput, listStack.get(listStack.size()-1));
        	listStack.remove(listStack.size()-1); // remove operation - xóa toán tử cuối cùng trong listStack sau khi tính
        }
        return listOutput.get(0).toString(); // sau khi tính toán xong, listOutput sẽ chỉ còn 1 phần tử -- chính là kết quả cần tìm
    }
 
    // Chạy chương trình
    @SuppressWarnings("resource")
	public static void main(String[] args)
    {
        // Test string:  5+2/(3-8)^5^2 ---- result : 5.0
    	
        String testString; // 5 + 2 / (3 - 8) ^5 ^2
        Scanner scanner = new Scanner(System.in);
		System.out.println("Nhập vào chuỗi cần tính:");
		testString = scanner.nextLine();
		
        // xóa dấu cách để nếu có nhập khoảng cách thì cũng tính đúng
		System.out.println("Chuỗi đã nhập sau khi xóa khoảng trống:");
		testString = testString.replaceAll("\\s+","");
		System.out.println(testString);
 
		System.out.println("Kết quả phép tính:(làm tròn tới số thập phân thứ nhất - 小数点以下第一位に整える) ");
        // Printing RPN for the above infix notation
        // Illustrating shunting yard algorithm
        System.out.println(infixToRpn(testString)); // 5238-5^2^/+
    }
}
