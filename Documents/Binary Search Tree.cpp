#include<stdio.h>
#include<stdlib.h>

#define FALSE_VALUE 0
#define TRUE_VALUE 1

struct treeNode
{
    int item;
    struct treeNode * left; //points to left child
    struct treeNode * right; //points to right child
};

struct treeNode * root;


void initializeTree()
{
    root = 0;
}


struct treeNode * searchItem(struct treeNode * node, int item)
{
    if(node==0) return 0;
    if(node->item==item) return node; //found, return node
    struct treeNode * t = 0;
    if(item < node->item)
        t = searchItem(node->left, item); //search in the left sub-tree
    else
        t = searchItem(node->right, item); //search in the right sub-tree
    return t;
};


struct treeNode * makeTreeNode(int item)
{
    struct treeNode * node ;
    node = (struct treeNode *)malloc(sizeof(struct treeNode));
    node->item = item;
    node->left = 0;
    node->right = 0;
    return node;
};

struct treeNode * insertItem(struct treeNode *node, int item)
{
    if(node==0) //insert as the root as the tree is empty
    {
        struct treeNode * newNode = makeTreeNode(item);
        root = newNode;
        return newNode;
    }

    if(node->item==item) return 0; //already an item exists, so return NULL

    if(item < node->item && node->left==0) //insert as the left child
    {
        struct treeNode * newNode = makeTreeNode(item);
        node->left = newNode;
        return newNode;
    }

    if(item > node->item && node->right==0) //insert as the right child
    {
        struct treeNode * newNode = makeTreeNode(item);
        node->right = newNode;
        return newNode;
    }

    if(item < node->item)  /// Recursive definition to traverse to end of tree shifting right or left
        return insertItem(node->left, item); //insert at left sub-tree
    else
        return insertItem(node->right, item); //insert at right sub-tree

}

int calcNodeHeight(struct treeNode * node) ///return height of a node
{
    if(node==0) return -1;
    int l, r;
    l = calcNodeHeight(node->left);
//    printf("%d  %d\n",l,r);
    r = calcNodeHeight(node->right);
//    printf("%d  %d\n",l,r);
    if(l>r) return l+1;
    else return r+1;
}


int calcHeight(int item) ///return height of an item in the tree
{
    struct treeNode * node = 0;
    node = searchItem(root, item);
    if(node==0) return -1; //not found
    else return calcNodeHeight(node);
}

int getSize(struct treeNode * node)
{
    if(node==0) return 1;

    //print left sub-tree
    int sl = getSize(node->left);

//    printf("%03d\n",node->item);

    int sr = getSize(node->right);
    return sl+sr;
}

int calcNodeDepth(struct treeNode * node) //return depth of a node
{
    if( node == root)
        return 0;
    struct treeNode *snode = root;
    int count =0;
    while(snode!=0){
    if(snode->item==node->item) return count; //found, return node

    if(snode->item < node->item){
        snode = snode->right;
        count++;
    }
    else{
        snode = snode->left;
        count++;
    }

    }
    return count;

}

int calcDepth(int item)//return depth of an item in the tree
{
    struct treeNode * node = 0;
    node = searchItem(root, item);
    if(node==0) return -1; //not found
    else return calcNodeDepth(node);
}

/*
struct treeNode* Successor(struct treeNode * node, int item) {
     //otherwise, go to right child, and then all left children
     struct treeNode * succ, * t;
     if( node->right != 0 ){
        succ = node->right ;
        while( succ->left!=0 ){
            succ = succ->left ;
        }
        return succ ;
    }
    else  {
    //search the item, during searching, track the successor
    succ = 0 ;
    t = root ;
    while( t!= 0 ){
        if (node->item == t->item ){
            return succ ;
        }
        else if (node->item < t->item ){
            succ = t ;
            t = t->left ;
        }
        else {
            t = t->right ;
        }
    }
    return 0 ;
    }
}


struct treeNode * deleteItem(struct treeNode * node, int item)
{
    if(node==0) return node;

    if(item < node->item){
       node->left =  deleteItem(node->left, item);
    }
    else if (item > node->item)
        node->right = deleteItem(node->right, item);
    else {
    // node with only one child or no child
        if (node->left == 0) {
            struct treeNode *temp = node->right;
            free(node);
            return temp;
        }
        else if (node->right == 0) {
        struct treeNode *temp = node->left;
        free(node);
        return temp;
        }
        struct treeNode *temp = node->right;
        while(temp->left !=0){
            temp = temp->left;
        }
        // Copy the inorder successor's content to this node
        node->item = temp->item;
        // Delete the inorder successor
        node->right = deleteItem(node->right, temp->item);
    }

    return node;

}
*/


int deleteItem(struct treeNode * node, int item)
{
//    struct treeNode * res = searchItem(node, item);
    struct treeNode * res = 0;
    res = searchItem(node, item);
    if(res==0){
        return 0;
    }


    struct treeNode * r = node;
    struct treeNode * temp;
    temp = node;

    if(node == 0){return 1;}

    while(r!=0){

    if(item < r->item){
        temp = r;
        r = r->left;
    }
    else if(item > r->item){
        temp = r;
        r = r->right;
    }

    if(r->item==item){
            if( (r->right == 0 && r->left == 0) && r == root){
                free(r);
                initializeTree();
                break;
            }
            if(r->left == 0 && r == root){
                root = r->right;
                free(r);
                break;
            }
            if(r->right == 0 && r == root){
                root = r->left;
                free(r);
                break;
            }
            if(r->right == 0){ // No right node for root
                if(temp->item >item){ // left node exists
                    temp->left = r->left;
                    free(r);
                    break;

                }
                else{
                    temp->right = r->left;
                    free(r);
                    break;
                }

            }
            else if(r->left == 0){
                if(temp->item >item){ // left node exists
                    temp->left = r->right;
                    free(r);
                    break;

                }
                else{
                    temp->right = r->right;
                    free(r);
                    break;
                }
            }
            else{
                temp = r;
                r = r->right;
                if(r->left == 0){
                        temp->item = r->item;
                    temp->right = r->right;
                    free(r);
                    break;
                }
                else{
                    struct treeNode *t;
                    while(r!=0){
                        t = r;
                        r = r->left;
                    }
                    temp->item = t->item;
                    deleteItem( temp->right, t->item);
                    break;
                }

            }

    }

//    printf("%d\n",temp->item);

}

return 1;

}

int getMinItem() //returns the minimum item in the tree
{
    struct treeNode *node = root;
    if(node == 0) return 0;
    while(node->left !=0){
        node = node->left;
    }
    return node->item;
    //write your codes here
}

int getMaxItem() //returns the maximum item in the tree
{
    struct treeNode *node = root;
    if(node == 0) return 0;
    while(node->right !=0){
        node = node->right;
    }
    return node->item;
    //write your codes here
}
int counter =0;
int rangeSearch(struct treeNode * node, int leftBound, int rightBound) //returns number of items in the
{
    if(leftBound == rightBound) return 1;
    if(node == 0) return 1;
    rangeSearch(node->left, leftBound ,rightBound);

    if(node->item >= leftBound && node->item <= rightBound){
        counter++;
    }
    else if(node->item >= rightBound){
        return counter;
    }
    //print item
//    printf("%03d %d\n",node->item , counter);


    //print right sub-tree
    rangeSearch(node->right, leftBound ,rightBound);

}

void printInOrder(struct treeNode * node, int height)
{
    if(node==0) return ;

    //print left sub-tree
    printInOrder(node->left, height-1);

    //print item
    for(int i=0;i<height;i++)printf("   "); ///Height needed for printing with space
    printf("%03d\n",node->item);

    //print right sub-tree
    printInOrder(node->right, height-1);
}

/*
void printpreorder(struct treeNode * node)
{
    if(node==0) return ;

    printf("%03d\n",node->item);

    //print left sub-tree
    printpreorder(node->left);

    //print item

    //print right sub-tree
    printpreorder(node->right);

}
*/

int main(void)
{
    initializeTree();

//    insertItem(root, 50);
//    insertItem(root, 39);
//    insertItem(root, 42);
//    insertItem(root, 41);
//    insertItem(root, 17);
//    insertItem(root, 9);
//    insertItem(root, 60);
//    insertItem(root, 62);
//    insertItem(root, 55);
//    insertItem(root, 68);
//    insertItem(root, 16);

    while(1)
    {
        printf("1. Insert item. 2. Delete item. 3. Search item. \n");
        printf("4. Print height of tree. 5. Print height of an item. \n");
        printf("6. PrintInOrder. 7. exit. 8. getSize. 9. Depth of item.\n");
        printf("10.Minterm. 11. Maxterm. 12. Range Search. \n");


        int ch;
        scanf("%d",&ch);
        if(ch==1)
        {
            int item;
            scanf("%d", &item);
            insertItem(root, item);
        }
        else if(ch==2)
        {
            int item;
            scanf("%d", &item);
            if(deleteItem(root, item)){
                printf("Deleted successfully\n");
            }
            else{
                printf("Not found\n");
            }
        }
        else if(ch==3)
        {
            int item;
            scanf("%d", &item);
            struct treeNode * res = searchItem(root, item);
            if(res!=0) printf("Found.\n");
            else printf("Not found.\n");
        }
        else if(ch==4)
        {
            int height = calcNodeHeight(root);
            printf("Height of tree = %d\n", height);
        }
        else if(ch==5)
        {
            int item;
            scanf("%d", &item);
            int height = calcHeight(item);
            printf("Height of %d = %d\n", item, height);
        }
        else if(ch==6)
        {
            int h = calcNodeHeight(root);
            printf("\n--------------------------------\n",h);
            printInOrder(root, h);
            printf("--------------------------------\n");
        }
        else if(ch==7)
        {
            break;
        }
        else if(ch==8)
        {
            int a = getSize(root);
            printf("Number of nodes : %d \n" ,a-1 );

        }
        else if(ch==9)
        {
            int item;
            scanf("%d",&item);
            int a = calcDepth(item);
            if(a == -1){
                printf("No item\n");
                continue;
            }
            printf("Depth of %d = %d\n",item , a);

        }
        else if(ch==10)
        {
            int a = getMinItem();
            printf("Minimum element = %d\n",a);
        }
        else if(ch==11)
        {
            int a = getMaxItem();
            printf("Maximum element = %d\n",a);
        }
        else if(ch==12)
        {
            counter =0;
            int up , low;
            scanf("%d%d",&up,&low);
            int a = rangeSearch(root , up , low);
            printf("Number of nodes = %d\n",a);

        }


    }

}
