package mini_proj;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoItem implements Comparable<TodoItem> {
    
    // 1. 변수
    private int id;             
    private String description;  
    private boolean isDone;     
    private LocalDate dueDate;   
    private String note;         // 참고 필드

    // 2. 생성자
    public TodoItem(int id, String description, LocalDate dueDate, String note) {
        this.id = id;
        this.description = description;
        this.isDone = false;
        this.dueDate = dueDate;
        this.note = note;
    }

    // 3. Getter/Setter
    public int getId() { return id; }
    public String getDescription() { return description; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }
    public LocalDate getDueDate() { return dueDate; }
    public String getNote() { return note; }

    // 4. toString() 메서드 오버라이딩 (목록 출력용: 참고 내용은 제외)
    @Override
    public String toString() {
        String status = isDone ? "[V]" : "[ ]"; 
        String dateString = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        return status + " " + id + ". " + description + " (마감일: " + dateString + ")";
    }
    
    // 5. Comparable 인터페이스 구현 (마감일 기준으로 정렬)
    @Override
    public int compareTo(TodoItem other) {
        return this.dueDate.compareTo(other.dueDate);
    }
}