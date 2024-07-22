package com.gj.gjchungsa.camp.ResultSerachKeyword;

import java.util.List;

public class Camp_mark_ResultSearchKeyword {
    PlaceMeta meta;
    List<Camp_mark_Place> documents;
}

class PlaceMeta{
    int total_count;
    int pageable_count;
    Boolean is_end;
    RegionInfo same_name;
}

class RegionInfo{
    List<String> region;
    String keyword;
    String selected_region;
}



