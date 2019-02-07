# WikiGameBot
Plays the "Wikipedia Game" using different searches. Scrape Wikipedia responsibly (or use Bidirectional BFS) until I implement the offline search 

The Wikipedia Game is simple-- How many links does it take to get from one page to another? (Some variants include Hitler or Jesus as the target page)

After choosing between several different search types (BFS, DFS, IDDFS, BiBFS), the program queries Wikipedia and collects all of the links on the page. Then, once finding any shortest path, it displays the path.

Originally I intended to display all considered pages, but too many queries were required. I plan on implementing an offline search feature to allow this while respecting Wikipedia's API.

TODO: Add pictures
