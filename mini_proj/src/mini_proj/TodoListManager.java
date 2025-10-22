package mini_proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TodoListManager {

    private List<TodoItem> todoList; 
    private Scanner scanner;         
    private int nextId = 1;          

    public TodoListManager() {
        this.todoList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // 1. 메인 실행 루프 (변경 없음)
    public void start() {
        System.out.println("⭐ 텍스트 기반 할 일 목록 관리 시스템을 시작합니다. ⭐");
        
        while (true) {
            displayMenu();
            int choice = getIntegerInputNotZero("메뉴를 선택하세요: ");

            switch (choice) {
                case 1:
                    addTodoItem();
                    break;
                case 2:
                    viewTodoList();
                    break;
                case 3:
                    markAsDone();
                    break;
                case 4:
                    deleteTodoItem(); // ⭐ 4번 메뉴 수정됨
                    break;
                case 7: 
                    exitProgram();
                    return; 
                default:
                    System.out.println("❌ 잘못된 메뉴 선택입니다. 1에서 4, 7 사이의 숫자를 입력해주세요.");
            }
            System.out.println("----------------------------------------------");
        }
    }

    // 메뉴 출력 (변경 없음)
    private void displayMenu() {
        System.out.println("\n===== To-Do List 메뉴 =====");
        System.out.println("1. 할 일 추가");
        System.out.println("2. 할 일 목록 보기 (상세 보기, 정렬, 필터링)"); 
        System.out.println("3. 할 일 완료 표시");
        System.out.println("4. 할 일 삭제"); 
        System.out.println("7. 프로그램 종료"); 
        System.out.println("===========================");
    }

    // 2. 할 일 추가 기능 (변경 없음)
    private void addTodoItem() {
        System.out.print("추가할 할 일 내용을 입력하세요 (0 입력 시 취소): ");
        scanner.nextLine();
        String description = scanner.nextLine().trim();
        
        if (description.equals("0")) {
            System.out.println("✅ 할 일 추가를 취소하고 이전 메뉴로 돌아갑니다.");
            return;
        }

        if (description.isEmpty()) {
            System.out.println("❌ 할 일 내용을 비워둘 수 없습니다.");
            return;
        }

        LocalDate dueDate = null;
        while (dueDate == null) {
            System.out.print("마감일을 입력하세요 (YYYY-MM-DD 형식, 0 입력 시 취소): ");
            String dateStr = scanner.nextLine().trim();
            
            if (dateStr.equals("0")) {
                System.out.println("✅ 할 일 추가를 취소하고 이전 메뉴로 돌아갑니다.");
                return;
            }
            
            try {
                dueDate = LocalDate.parse(dateStr); 
            } catch (DateTimeParseException e) {
                System.out.println("❌ 날짜 형식이 잘못되었습니다. YYYY-MM-DD 형식으로 다시 입력해주세요.");
            }
        }

        System.out.print("참고 내용을 입력하세요 (선택 사항, 0 입력 시 취소): ");
        String note = scanner.nextLine().trim(); 
        
        if (note.equals("0")) {
             System.out.println("✅ 할 일 추가를 취소하고 이전 메뉴로 돌아갑니다.");
            return;
        }
        
        TodoItem newItem = new TodoItem(nextId++, description, dueDate, note);
        todoList.add(newItem);
        System.out.println("✅ 할 일이 성공적으로 추가되었습니다! (ID: " + newItem.getId() + ")");
    }
    
    // 3. 할 일 목록 보기/필터링/정렬/상세 보기 통합 기능 (변경 없음)
    private void viewTodoList() {
        if (todoList.isEmpty()) {
            System.out.println("✨ 할 일 목록이 비어 있습니다. 새로운 할 일을 추가해주세요.");
            return;
        }

        List<TodoItem> currentList = new ArrayList<>(todoList);
        
        while (true) {
            printList(currentList); 

            System.out.println("\n----- 상세/정렬/필터 메뉴 -----");
            System.out.println("a. 미완료된 할 일만 보기 (필터)");
            System.out.println("b. 마감일 기준으로 정렬하기");
            System.out.println("c. 전체 목록 보기 (필터 해제)");
            System.out.println("0. 메인 메뉴로 돌아가기"); 
            System.out.println("-----------------------------");
            System.out.println("자세히 보려면 ID 번호 또는 메뉴 (a, b, c, 0)를 입력하세요: "); 

            String subChoiceStr = scanner.next().trim().toLowerCase(); 
            scanner.nextLine();
            
            if (subChoiceStr.equals("0")) { 
                System.out.println("메인 메뉴로 돌아갑니다.");
                break;
            } else if (subChoiceStr.equals("a")) { 
                currentList = filterPending(todoList);
            } else if (subChoiceStr.equals("b")) { 
                currentList = sortList(currentList);
            } else if (subChoiceStr.equals("c")) { 
                currentList = new ArrayList<>(todoList);
            } else {
                try {
                    int id = Integer.parseInt(subChoiceStr);
                    viewTodoDetail(id); 
                } catch (NumberFormatException e) {
                    System.out.println("❌ 잘못된 입력입니다. ID 번호 또는 메뉴 (a, b, c, 0)를 다시 입력해주세요.");
                }
            }
        }
    }
    
    // 4. 리스트 출력 메서드 (변경 없음)
    private void printList(List<TodoItem> list) {
        // ... (이전 코드와 동일)
         if (list.isEmpty()) {
            System.out.println("⚠️ 현재 목록에 해당하는 할 일이 없습니다.");
            return;
        }
        System.out.println("\n----- To-Do List (" + list.size() + "개) -----");
        for (TodoItem item : list) {
            System.out.println(item); 
        }
        System.out.println("----------------------------------------------");
    }

    // 5. 상세 보기 메서드 (변경 없음)
    private void viewTodoDetail(int id) {
        // ... (이전 코드와 동일)
        for (TodoItem item : todoList) { 
            if (item.getId() == id) {
                System.out.println("\n===== ID: " + id + " 상세 정보 =====");
                System.out.println(" 상태: " + (item.isDone() ? "완료" : "미완료"));
                System.out.println(" 내용: " + item.getDescription());
                System.out.println(" 마감일: " + item.getDueDate());
                
                if (!item.getNote().isEmpty()) {
                    System.out.println(" 참고: " + item.getNote());
                } else {
                    System.out.println(" 참고: (작성된 참고 내용 없음)");
                }
                System.out.println("==================================");
                return;
            }
        }
        System.out.println("❌ ID [" + id + "]에 해당하는 할 일을 찾을 수 없습니다.");
    }
    
    // 6. 미완료 필터링 로직 (변경 없음)
    private List<TodoItem> filterPending(List<TodoItem> sourceList) {
        // ... (이전 코드와 동일)
        List<TodoItem> pendingList = new ArrayList<>();
        for (TodoItem item : sourceList) {
            if (!item.isDone()) {
                pendingList.add(item);
            }
        }
        System.out.println("✅ 미완료 항목으로 필터링되었습니다.");
        return pendingList;
    }
    
    // 7. 정렬 로직 (변경 없음)
    private List<TodoItem> sortList(List<TodoItem> sourceList) {
        // ... (이전 코드와 동일)
        List<TodoItem> sortedList = new ArrayList<>(sourceList);
        Collections.sort(sortedList);
        System.out.println("✅ 마감일 기준으로 정렬되었습니다.");
        return sortedList;
    }
    
    // 8. 할 일 완료 표시 기능 (변경 없음)
    private void markAsDone() {
        // ... (이전 코드와 동일)
        printList(new ArrayList<>(todoList)); 
        if (todoList.isEmpty()) return;

        int id = getIntegerInput("완료할 할 일의 ID를 입력하세요 (0 입력 시 취소): ");
        if (id == 0) return;
        
        for (TodoItem item : todoList) {
            if (item.getId() == id) {
                if (item.isDone()) {
                    System.out.println("⚠️ 이미 완료된 항목입니다. (ID: " + id + ")");
                } else {
                    item.setDone(true); 
                    System.out.println("🎉 할 일 완료! [" + item.getDescription() + "]의 상태가 완료로 변경되었습니다.");
                }
                return; 
            }
        }
        
        System.out.println("❌ ID [" + id + "]에 해당하는 할 일을 찾을 수 없습니다.");
    }

    // 9. 할 일 삭제 기능 (메인 메뉴 4번) - ⭐ 대폭 수정
    private void deleteTodoItem() {
        if (todoList.isEmpty()) {
            System.out.println("✨ 할 일 목록이 비어 있어 삭제할 항목이 없습니다.");
            return;
        }
        
        printList(new ArrayList<>(todoList)); 
        
        System.out.println("\n----- 할 일 삭제 메뉴 -----");
        System.out.println("a. 완료된 항목 전체 삭제");
        System.out.println("0. 이전 메뉴로 돌아가기");
        System.out.println("--------------------------");
        System.out.print("삭제할 할 일의 ID 번호 또는 메뉴 (a, 0)를 입력하세요: ");
        
        // 문자열 입력 받기
        String deleteChoiceStr = scanner.next().trim().toLowerCase();
        scanner.nextLine(); // 버퍼 비우기

        if (deleteChoiceStr.equals("0")) {
            System.out.println("✅ 삭제를 취소하고 이전 메뉴로 돌아갑니다.");
            return;
        } else if (deleteChoiceStr.equals("a")) { // 완료된 항목 전체 삭제
            deleteCompletedItems();
            return;
        } else {
            // ID 번호로 간주하여 개별 삭제 처리
            try {
                int id = Integer.parseInt(deleteChoiceStr);
                
                for (int i = 0; i < todoList.size(); i++) {
                    if (todoList.get(i).getId() == id) {
                        TodoItem removedItem = todoList.remove(i);
                        System.out.println("🗑️ 할 일이 성공적으로 삭제되었습니다: [" + removedItem.getDescription() + "]");
                        return; 
                    }
                }
                System.out.println("❌ ID [" + id + "]에 해당하는 할 일을 찾을 수 없습니다.");
                
            } catch (NumberFormatException e) {
                System.out.println("❌ 잘못된 입력입니다. ID 번호 또는 메뉴 (a, 0)를 다시 입력해주세요.");
            }
        }
    }
    
    // 10. 완료된 항목 전체 삭제 메서드 (새로 추가)
    private void deleteCompletedItems() {
        int initialSize = todoList.size();
        
        // removeIf 메서드를 사용하여 조건에 맞는 항목을 모두 제거 (Java 8 이상 지원)
        boolean removed = todoList.removeIf(TodoItem::isDone);
        
        if (removed) {
            int deletedCount = initialSize - todoList.size();
            System.out.println("🎉 완료된 항목 " + deletedCount + "개가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("⚠️ 현재 완료된 항목이 없어 삭제할 항목이 없습니다.");
        }
    }

    // 11. 프로그램 종료 (변경 없음)
    private void exitProgram() {
        System.out.println("프로그램을 종료합니다. 다음에 또 만나요! 👋");
        scanner.close(); 
    }
    
    // 12. 정수 입력 유틸리티 메서드 (0 입력 시 이전 메뉴로 돌아가기)
    private int getIntegerInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("❌ 오류: 유효한 정수(숫자)를 입력해야 합니다.");
            scanner.next(); 
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        if (input == 0) {
            System.out.println("✅ 작업을 취소하고 이전 메뉴로 돌아갑니다.");
        }
        return input;
    }
    
    // 13. 정수 입력 유틸리티 메서드 (0을 입력받지 않는 경우)
    private int getIntegerInputNotZero(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("❌ 오류: 유효한 정수(숫자)를 입력해야 합니다.");
            scanner.next(); 
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }
    
    // main 메서드
    public static void main(String[] args) {
        TodoListManager manager = new TodoListManager();
        manager.start();
    }
}