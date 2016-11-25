package squaring.vitrox.privalia.Common;

public class PaginationHelper {

    private int TotalPages=-1;

    private int CurrentPage=0;

    private int MaxItems;

    public PaginationHelper(int totalPages, int maxitems) {
        TotalPages = totalPages;
        MaxItems= maxitems;
    }

    public PaginationHelper(){

    }
    public int getCurrentPage() {
            return CurrentPage;
    }

    public int getNextPage()
    {
        if(!isLastPage()) {
            CurrentPage++;
        }
        return CurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    public boolean isLastPage()
    {
       return  (CurrentPage == TotalPages);
    }

    public int getMaxItems() {
        return MaxItems;
    }
}
