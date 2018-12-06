import java.util.*;

/**
 * Created by dande_000 on 12/5/2018.
 */
public class Searches {
    public static ArrayList<String> DFS(String url, String goal, int depthRem)
    {
        //System.out.println(url);
        if (url.equals(goal))
        {
            ArrayList<String> al = new ArrayList<String>();
            al.add(0, url);
            return al;
        }
        if (depthRem <= 0)
        {
            return null;
        }
        Set<String> s = Scraper.scrape(url);

        for (String check : s)
        {
            ArrayList<String> al = DFS(check, goal, depthRem-1);
            if (al != null) {
                al.add(0, url);
                return al;
            }
        }
        return null;
    }

    public static ArrayList<String> IDDFS(String url, String goal)
    {
        ArrayList<String> path;
        for (int i = 0; i < 10; i++)
        {
            System.out.println(i);
            path = DFS(url, goal, i);
            if (path != null)
                return path;
        }
        return null;
    }

    public static ArrayList<String> BFS(String url, String goal)
    {
        Set<String> visited = new HashSet<>();
        Queue<Page> visitMe = new LinkedList<>();
        visited.add(url);
        visitMe.add(new Page(url, null));
        while (!(visitMe.isEmpty() || visitMe.peek().url.equals(goal)))
        {
            Page next = visitMe.poll();
            System.out.println(next.url);
            Set<String> s = Scraper.scrape(next.url);
            for (String check : s)
            {

                if (visited.contains(s))
                    continue;
                visited.add(url);
                visitMe.add(new Page(check, next));
            }
        }
        if (visitMe.peek().url.equals(goal))
        {
            ArrayList<String> ans = new ArrayList<>();
            Page p = visitMe.peek();
            while (p != null)
            {
                ans.add(0, p.url);
                p = p.parent;
            }
            return ans;
        }
        return null;
    }

    public static ArrayList<String> BiBFS(String url, String goal)
    {
        Set<Page> visitedF = new HashSet<>();
        Set<Page> visitedR = new HashSet<>();
        Queue<Page> visitMeF = new LinkedList<>();
        Queue<Page> visitMeR = new LinkedList<>();

        visitedF.add(new Page(url));
        visitMeF.add(new Page(url, null));
        visitedR.add(new Page(goal));
        visitMeR.add(new Page(goal, null));
        long i = 0;
        while (!visitMeF.isEmpty() && ! visitMeR.isEmpty()) {
            System.out.println(i);
            if (!visitMeF.isEmpty()) {
                i += 1;
                Page next = visitMeF.poll();
                System.out.println(next);
                if (next.url.equals(goal)) {
                    System.out.println("Trivial F");
                    ArrayList<String> ans = new ArrayList<>();
                    Page p = next;
                    while (p != null) {
                        ans.add(0, p.url);
                        p = p.parent;
                    }
                    return ans;
                }
                else if (visitedR.contains(next))
                {
                    System.out.println("nonTrivial F");
                    ArrayList<String> ans = new ArrayList<>();
                    Page p = next;
                    while (p != null) {
                        ans.add(0, p.url);
                        p = p.parent;
                    }
                    p = visitedR.stream().filter(e -> e.equals(next)).findFirst().orElse(null);
                    if (p == null) System.out.println("D:");
                    while (p != null) {
                        ans.add(p.url);
                        p = p.parent;
                    }
                    return ans;
                }
                Set<String> s = Scraper.scrape(next.url);
                for (String check : s) {
                    if (visitedR.contains(new Page(check))) {
                        System.out.println("nonTrivial F2");
                        ArrayList<String> ans = new ArrayList<>();
                        Page p = next;
                        while (p != null) {
                            ans.add(0, p.url);
                            p = p.parent;
                        }
                        p = visitMeR.stream().filter(e -> e.url.equals(check)).findFirst().orElse(null);
                        if (p == null) System.out.println("D:");
                        while (p != null) {
                            ans.add(p.url);
                            p = p.parent;
                        }
                        return ans;
                    }
                    if (visitedF.contains(new Page(check)))
                        continue;
                    visitedF.add(new Page(check, next));
                    visitMeF.add(new Page(check, next));
                }
            }
            if (!visitMeR.isEmpty()) {
                i += 1;
                Page next = visitMeR.poll();
                if (next.url.equals(url)) {
                    System.out.println("Trivial R");
                    ArrayList<String> ans = new ArrayList<>();
                    Page p = next;
                    while (p != null) {
                        ans.add(p.url);
                        p = p.parent;
                    }
                    return ans;

                } else if (visitedF.contains(next))
                {
                    System.out.println("nonTrivial R");
                    ArrayList<String> ans = new ArrayList<>();
                    Page p = next;
                    Page tempP;
                    while (p != null) {
                        ans.add(p.url);
                        p = p.parent;
                    }
                    p = visitedF.stream().filter(e -> e.equals(next)).findFirst().orElse(null);
                    if (p == null) System.out.println("D:");
                    while (p != null) {
                        ans.add(0, p.url);
                        p = p.parent;
                    }
                    return ans;
                }
                Set<String> s = Scraper.scrape(Scraper.reverseLink(next.url));
                for (String check : s) {
                    if (visitedF.contains(new Page(check)))
                    {
                        System.out.println("nonTrivial R2");
                        ArrayList<String> ans = new ArrayList<>();
                        Page p = next;
                        while (p != null) {
                            ans.add(p.url);
                            p = p.parent;
                        }
                        p = visitMeF.stream().filter(e -> e.url.equals(check)).findFirst().orElse(null);
                        if (p == null) System.out.println("D:");
                        while (p != null) {
                            ans.add(0, p.url);
                            p = p.parent;
                        }
                        return ans;
                    }

                    if (visitedR.contains(new Page(check)))
                        continue;
                    visitedR.add(new Page(check, next));
                    visitMeR.add(new Page(check, next));
                }
            }

        }
        return null;
    }
}
