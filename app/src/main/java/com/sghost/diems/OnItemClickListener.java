package com.sghost.diems;

import java.io.IOException;

interface OnItemClickListener {
    void onItemClick(int position);
    void onDownloadClick(int position) throws IOException;
    void onShowClick(int position);
}

