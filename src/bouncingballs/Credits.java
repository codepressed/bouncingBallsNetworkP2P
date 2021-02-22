package bouncingballs;

public class Credits {
    String name;
    String email;
    String course_Info;
    String fps;

    /**
     * This Class is exclusively to save my signature data
     */
    public Credits(){
        name = "Dani Apesteguia";
        email = "ascipendcip@gmail.com";
        course_Info = "DAM 2 ♥♥♥ JUMI ♥♥♥";
        fps = "FPS: 60";
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse_Info() {
        return course_Info;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }
}
