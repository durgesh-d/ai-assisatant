package com.aicareer.resume.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIService
{

    @Value("${groq.api.key}")
    private String apiKey;

    private final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    // ATS RESUME ANALYSIS
    public String analyzeResume(String resumeText)
    {
        if(isInvalid(resumeText)) return "❌ Please provide valid resume text";

        String prompt = """
You are an ATS system. Analyze resume and give STRICT output:

Resume Score: XX/100

Strengths:
✔ point
✔ point
✔ point

Weaknesses:
✖ point
✖ point

Improvements:
👉 point
👉 point

Missing Keywords:
- keyword
- keyword

Summary:
2 lines only

Rules:
- No paragraphs
- Only bullet points
- Short output
""" + resumeText;

        return callAI(prompt, false);
    }

    // 🔥 JOB MATCH
    public String matchResumeWithJob(String resumeText, String jobDesc) {
        if(isInvalid(resumeText) || isInvalid(jobDesc))
            return "❌ Resume or Job Description missing";

        String prompt = """
Compare resume with job description. Give STRICT output:

Match Score: XX/100

Matched Skills:
✔ skill
✔ skill

Missing Skills:
✖ skill
✖ skill

Suggestions:
👉 suggestion
👉 suggestion

Missing Keywords:
- keyword

Summary:
2 lines about fit

Rules:
- Bullet points only
- No paragraphs
""" + resumeText + "\n\nJob Description:\n" + jobDesc;

        return callAI(prompt, false);
    }

    //  PORTFOLIO
    public String generatePortfolio(String resumeText)
    {
        if(isInvalid(resumeText)) return "❌ Resume required for portfolio";

        String prompt = """
You are expert frontend developer. Create stunning portfolio website.

OUTPUT RULE: Return ONLY raw HTML starting with <!DOCTYPE html>
NO explanation. NO markdown. NO ``` blocks. Just pure HTML.

Extract from resume: Name, Email, Phone, Skills, Projects, Experience, Education.

Use this EXACT HTML structure and CSS:

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>[NAME] Portfolio</title>
<link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<style>
:root {
  --bg: #0a0e1a;
  --bg2: #0f1628;
  --primary: #00ffcc;
  --secondary: #00ccff;
  --purple: #7c3aed;
  --card: rgba(255,255,255,0.05);
  --border: rgba(0,255,200,0.15);
  --text: #e0e0e0;
  --text2: #888;
}
* { margin:0; padding:0; box-sizing:border-box; }
html { scroll-behavior:smooth; }
body { font-family:'Poppins',sans-serif; background:var(--bg); color:var(--text); overflow-x:hidden; }
::-webkit-scrollbar { width:6px; }
::-webkit-scrollbar-track { background:var(--bg); }
::-webkit-scrollbar-thumb { background:var(--primary); border-radius:3px; }
nav {
  position:fixed; top:0; width:100%; z-index:1000;
  background:rgba(10,14,26,0.95); backdrop-filter:blur(20px);
  border-bottom:1px solid var(--border);
  display:flex; justify-content:space-between; align-items:center;
  padding:15px 60px;
}
.nav-logo {
  font-size:1.3rem; font-weight:700;
  background:linear-gradient(90deg,var(--primary),var(--secondary));
  -webkit-background-clip:text; -webkit-text-fill-color:transparent;
}
.nav-links { display:flex; gap:35px; list-style:none; }
.nav-links a { color:var(--text2); text-decoration:none; font-size:0.9rem; transition:0.3s; }
.nav-links a:hover { color:var(--primary); }
#home {
  min-height:100vh; display:flex; align-items:center; justify-content:center;
  text-align:center; padding:100px 20px 50px; position:relative; overflow:hidden;
  background:radial-gradient(ellipse at 20% 50%,rgba(0,255,200,0.06) 0%,transparent 60%),
             radial-gradient(ellipse at 80% 20%,rgba(0,150,255,0.06) 0%,transparent 60%);
}
.hero-content { position:relative; z-index:2; }
.hero-greeting { font-size:1.1rem; color:var(--primary); letter-spacing:3px; text-transform:uppercase; margin-bottom:15px; }
.hero-name {
  font-size:3.8rem; font-weight:800; line-height:1.1; margin-bottom:20px;
  background:linear-gradient(135deg,#ffffff,var(--primary),var(--secondary));
  -webkit-background-clip:text; -webkit-text-fill-color:transparent;
}
.typing-wrapper { font-size:1.2rem; color:var(--primary); margin-bottom:15px; min-height:35px; }
.hero-tagline { color:var(--text2); font-size:1rem; margin-bottom:35px; }
.hero-buttons { display:flex; gap:15px; justify-content:center; flex-wrap:wrap; margin-bottom:40px; }
.btn-primary {
  background:linear-gradient(135deg,var(--primary),var(--secondary));
  color:#0a0e1a; padding:13px 35px; border-radius:30px;
  font-weight:700; border:none; cursor:pointer; font-family:'Poppins',sans-serif;
  font-size:0.9rem; transition:0.3s; text-decoration:none; display:inline-block;
}
.btn-primary:hover { transform:translateY(-3px); box-shadow:0 10px 30px rgba(0,255,200,0.4); }
.btn-secondary {
  background:transparent; border:2px solid var(--primary); color:var(--primary);
  padding:11px 35px; border-radius:30px; cursor:pointer; font-family:'Poppins',sans-serif;
  font-size:0.9rem; transition:0.3s; text-decoration:none; display:inline-block;
}
.btn-secondary:hover { background:rgba(0,255,200,0.1); transform:translateY(-3px); }
.social-links { display:flex; gap:15px; justify-content:center; }
.social-link {
  width:45px; height:45px; border-radius:50%;
  background:var(--card); border:1px solid var(--border);
  display:flex; align-items:center; justify-content:center;
  color:var(--primary); text-decoration:none; font-size:1.1rem; transition:0.3s;
}
.social-link:hover { background:var(--primary); color:#0a0e1a; transform:translateY(-3px); }
.shape { position:absolute; border-radius:50%; filter:blur(80px); animation:float 6s ease-in-out infinite; }
.shape1 { width:300px; height:300px; background:rgba(0,255,200,0.08); top:-100px; right:-100px; }
.shape2 { width:200px; height:200px; background:rgba(0,150,255,0.08); bottom:50px; left:-50px; animation-delay:2s; }
.shape3 { width:150px; height:150px; background:rgba(124,58,237,0.08); top:50%; right:10%; animation-delay:4s; }
@keyframes float { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-20px)} }
section { padding:100px 60px; }
.section-title { text-align:center; margin-bottom:60px; }
.section-title h2 { font-size:2.2rem; font-weight:700; color:white; }
.section-title h2 span { color:var(--primary); }
.section-title p { color:var(--text2); margin-top:10px; }
.section-line { width:60px; height:3px; background:linear-gradient(90deg,var(--primary),var(--secondary)); border-radius:2px; margin:15px auto 0; }
#about { background:var(--bg2); }
.about-grid { display:grid; grid-template-columns:1fr 1fr; gap:60px; align-items:center; }
.about-text h3 { font-size:1.8rem; font-weight:700; margin-bottom:20px; }
.about-text h3 span { color:var(--primary); }
.about-text p { color:var(--text2); line-height:1.9; font-size:0.95rem; margin-bottom:15px; }
.stats-grid { display:grid; grid-template-columns:1fr 1fr; gap:20px; }
.stat-card { background:var(--card); border:1px solid var(--border); border-radius:16px; padding:25px; text-align:center; transition:0.3s; }
.stat-card:hover { border-color:var(--primary); transform:translateY(-5px); }
.stat-number { font-size:2.5rem; font-weight:800; color:var(--primary); display:block; }
.stat-label { color:var(--text2); font-size:0.85rem; margin-top:5px; }
#skills { background:var(--bg); }
.skills-grid { display:grid; grid-template-columns:repeat(3,1fr); gap:20px; }
.skill-card { background:var(--card); border:1px solid var(--border); border-radius:14px; padding:20px; transition:0.3s; }
.skill-card:hover { border-color:var(--primary); transform:translateY(-3px); box-shadow:0 5px 20px rgba(0,255,200,0.1); }
.skill-header { display:flex; align-items:center; gap:12px; margin-bottom:12px; }
.skill-icon { font-size:1.5rem; color:var(--primary); }
.skill-name { font-weight:600; font-size:0.9rem; }
.skill-bar { background:rgba(255,255,255,0.08); height:6px; border-radius:3px; overflow:hidden; }
.skill-fill { height:100%; background:linear-gradient(90deg,var(--primary),var(--secondary)); border-radius:3px; width:0%; transition:width 1.5s ease; }
#projects { background:var(--bg2); }
.projects-grid { display:grid; grid-template-columns:repeat(2,1fr); gap:25px; }
.project-card { background:var(--card); border:1px solid var(--border); border-radius:16px; overflow:hidden; transition:0.3s; }
.project-card:hover { border-color:var(--primary); transform:translateY(-5px); box-shadow:0 10px 30px rgba(0,255,200,0.1); }
.project-header { height:120px; background:linear-gradient(135deg,rgba(0,255,200,0.15),rgba(0,150,255,0.15)); display:flex; align-items:center; justify-content:center; font-size:2.5rem; }
.project-body { padding:20px; }
.project-name { font-size:1.1rem; font-weight:700; margin-bottom:8px; }
.project-desc { color:var(--text2); font-size:0.85rem; line-height:1.7; margin-bottom:15px; }
.tech-chips { display:flex; flex-wrap:wrap; gap:8px; margin-bottom:15px; }
.chip { background:rgba(0,255,200,0.1); border:1px solid rgba(0,255,200,0.2); color:var(--primary); padding:3px 12px; border-radius:20px; font-size:0.75rem; }
.project-links { display:flex; gap:10px; }
.project-btn { padding:8px 18px; border-radius:8px; font-size:0.8rem; cursor:pointer; font-family:'Poppins',sans-serif; transition:0.3s; text-decoration:none; display:inline-flex; align-items:center; gap:6px; }
.project-btn.github { background:rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.15); color:var(--text); }
.project-btn.github:hover { border-color:var(--primary); color:var(--primary); }
.project-btn.demo { background:linear-gradient(135deg,var(--primary),var(--secondary)); border:none; color:#0a0e1a; font-weight:600; }
#contact { background:var(--bg); }
.contact-grid { display:grid; grid-template-columns:repeat(3,1fr); gap:20px; max-width:800px; margin:0 auto; }
.contact-card { background:var(--card); border:1px solid var(--border); border-radius:16px; padding:30px 20px; text-align:center; transition:0.3s; }
.contact-card:hover { border-color:var(--primary); transform:translateY(-5px); }
.contact-icon { font-size:2rem; color:var(--primary); margin-bottom:12px; }
.contact-label { font-size:0.8rem; color:var(--text2); margin-bottom:6px; }
.contact-value { font-size:0.9rem; font-weight:500; word-break:break-all; }
.contact-socials { display:flex; gap:15px; justify-content:center; margin-top:40px; }
footer { background:var(--bg2); border-top:1px solid var(--border); padding:30px 60px; text-align:center; color:var(--text2); font-size:0.85rem; }
footer span { color:var(--primary); }
.reveal { opacity:0; transform:translateY(30px); transition:0.6s ease; }
.reveal.visible { opacity:1; transform:translateY(0); }
@media(max-width:768px) {
  nav { padding:15px 20px; }
  .nav-links { display:none; }
  .hero-name { font-size:2.5rem; }
  section { padding:60px 20px; }
  .about-grid { grid-template-columns:1fr; }
  .skills-grid { grid-template-columns:repeat(2,1fr); }
  .projects-grid { grid-template-columns:1fr; }
  .contact-grid { grid-template-columns:1fr; }
}
</style>
</head>
<body>
Use ABOVE CSS exactly. Fill content using resume data.
Build complete HTML body with all 7 sections.
Extract REAL data: name, skills with percentages, projects with descriptions.
RESUME DATA:
""" + resumeText;

        return callAI(prompt, true);
    }

    // 🔥 COVER LETTER
    public String generateCoverLetter(String resumeText, String jobDesc) {
        if(isInvalid(resumeText) || isInvalid(jobDesc))
            return "❌ Resume or Job Description missing";

        String prompt = """
Write a professional cover letter. Give STRICT output:

COVER_START
[Candidate Name]
[Email] | [Phone] | [Location]
[Date]

[Company Name] Hiring Team,

Introduction:
[2-3 lines about who you are and why applying]

Why I'm a Great Fit:
✔ [matched skill 1]
✔ [matched skill 2]
✔ [matched skill 3]

What I Bring:
👉 [value 1]
👉 [value 2]

Closing:
[2 lines professional closing]

Sincerely,
[Name]
COVER_END

Rules:
- Professional tone
- Use actual data from resume
- Match with job description
- Keep concise
""" + resumeText + "\n\nJob:\n" + jobDesc;

        return callAI(prompt, false);
    }

    // 🔥 AI CHAT
    public String chat(String message, String context) {

        String systemPrompt = """
You are an expert AI Career Assistant. You help with:
- Resume writing and improvement
- Interview preparation
- Career advice
- Job search strategies
- Technical skills guidance
- Cover letter writing
- Salary negotiation tips

Be helpful, concise, and professional.
If user asks about their resume, use the context provided.
Respond in the same language the user uses (English/Hindi/Marathi).
Keep responses clear and well formatted.
""";

        String userMessage = context.isEmpty()
            ? message
            : "My Resume Context:\n" + context + "\n\nQuestion: " + message;

        try {
            JSONObject request = new JSONObject();
            request.put("model", "llama-3.3-70b-versatile");
            request.put("max_tokens", 1000);

            JSONArray messages = new JSONArray();

            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);
            messages.put(systemMsg);

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.put(userMsg);

            request.put("messages", messages);

            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers =
                    new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            org.springframework.http.HttpEntity<String> entity =
                    new org.springframework.http.HttpEntity<>(request.toString(), headers);

            String response = restTemplate.postForObject(API_URL, entity, String.class);

            if (response == null || response.isEmpty()) return "❌ Empty response";

            JSONObject json = new JSONObject(response);
            return json.getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: " + e.getMessage();
        }
    }

    // COMMON METHOD
    private String callAI(String prompt, boolean isPortfolio)
    {
        try {
            JSONObject request = new JSONObject();

            if(isPortfolio) {
                request.put("model", "llama-3.3-70b-versatile");
                request.put("max_tokens", 8000);
            } else {
                request.put("model", "llama-3.1-8b-instant");
                request.put("max_tokens", 2000);
            }

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            request.put("messages", messages);

            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers =
                    new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            org.springframework.http.HttpEntity<String> entity =
                    new org.springframework.http.HttpEntity<>(request.toString(), headers);

            String response = restTemplate.postForObject(API_URL, entity, String.class);

            if(response == null || response.isEmpty()) return "❌ Empty response from AI";

            JSONObject json = new JSONObject(response);
            String content = json.getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content");

            if(isPortfolio)
            {
                content = content.replaceAll("(?s)```html", "")
                                 .replaceAll("(?s)```", "")
                                 .trim();
                int htmlStart = content.indexOf("<!DOCTYPE");
                if(htmlStart > 0) content = content.substring(htmlStart);
            }

            return content;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "❌ AI Error: " + e.getMessage();
        }
    }

    private boolean isInvalid(String text) 
    {
        return text == null || text.trim().isEmpty();
    }
    
 // RESUME TEMPLATE GENERATOR
    public String generateResumeTemplate(String templateId, String userData)
    {

        String styleGuide = switch (templateId) 
        		{
            case "modern" -> """
                STYLE: Modern Dark
                - Background: #0a0e1a
                - Primary: #00ffcc
                - Font: Poppins
                - Layout: Single column with left accent bar
                - Cards with glassmorphism
                """;
            case "professional" -> """
                STYLE: Professional Clean
                - Background: #ffffff
                - Primary: #2563eb
                - Font: Inter
                - Layout: Two column (sidebar + main)
                - Clean minimal design
                """;
            case "creative" -> """
                STYLE: Creative Purple
                - Background: #1a0a2e
                - Primary: #a78bfa
                - Font: Poppins
                - Layout: Creative asymmetric
                - Bold headers with gradient
                """;
            case "minimal" -> """
                STYLE: Minimal White
                - Background: #fafafa
                - Primary: #111111
                - Font: Inter
                - Layout: Ultra minimal single column
                - Clean typography only
                """;
            case "executive" -> """
                STYLE: Executive Gold
                - Background: #0f0f0f
                - Primary: #ffd700
                - Font: Playfair Display + Inter
                - Layout: Elegant two column
                - Gold accents throughout
                """;
            default -> """
                STYLE: Modern Dark
                - Background: #0a0e1a
                - Primary: #00ffcc
                """;
        };

        String prompt = """
    You are an expert resume designer. Create a stunning, professional resume HTML page.

    TEMPLATE STYLE:
    """ + styleGuide + """

    STRICT OUTPUT RULES:
    - Return ONLY raw HTML starting with <!DOCTYPE html>
    - NO explanation, NO markdown, NO ``` blocks
    - ALL CSS inside <style> tag
    - Fully responsive
    - Print-ready (use @media print)
    - Professional typography
    - Real sections: Header, Summary, Skills, Experience, Projects, Education, Contact

    IMPORTANT:
    - Extract and use ALL real data from user input
    - Make it look like a real professional resume
    - Add subtle animations for web view
    - @media print: remove animations, clean layout

    USER DATA:
    """ + userData;

        return callAI(prompt, true);
    }
}