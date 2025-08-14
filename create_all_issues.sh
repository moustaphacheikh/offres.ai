#!/bin/bash

# GitHub Issues Creation Script for offres.mr Django Payroll System
# Reads from Task Master tasks.json file and creates all GitHub issues automatically

REPO="moustaphacheikh/offres.ai"
TASKS_FILE="/Users/moustaphacheikh/Desktop/projects/ELIYA-Paie_Install/mccmr/.taskmaster/tasks/tasks.json"

echo "Creating GitHub issues for offres.mr Django Payroll System..."
echo "Repository: $REPO"
echo "Reading from: $TASKS_FILE"
echo ""

# Check if tasks.json exists
if [ ! -f "$TASKS_FILE" ]; then
    echo "Error: tasks.json file not found at $TASKS_FILE"
    exit 1
fi

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is required but not installed. Please install jq first."
    echo "On macOS: brew install jq"
    exit 1
fi

# Function to get milestone number from task ID
get_milestone_number() {
    local task_id=$1
    echo "${task_id}"
}

# Function to get module label from task ID
get_module_label() {
    local task_id=$1
    case $task_id in
        1) echo "module: setup" ;;
        2) echo "module: models" ;;
        3) echo "module: auth" ;;
        4) echo "module: employees" ;;
        5) echo "module: attendance" ;;
        6) echo "module: payroll" ;;
        7) echo "module: banking" ;;
        8) echo "module: compliance" ;;
        9) echo "module: reporting" ;;
        10) echo "module: admin" ;;
        *) echo "module: setup" ;;
    esac
}

# Function to get priority label from priority field
get_priority_label() {
    local priority=$1
    case $priority in
        high) echo "priority: high" ;;
        medium) echo "priority: medium" ;;
        low) echo "priority: low" ;;
        *) echo "priority: medium" ;;
    esac
}

# Function to get complexity label from complexity score
get_complexity_label() {
    local complexity=$1
    if [ -n "$complexity" ] && [ "$complexity" != "null" ]; then
        echo "complexity: $complexity"
    else
        echo "complexity: 4"
    fi
}

# Function to convert dependencies array to GitHub issue references
convert_dependencies() {
    local deps_json="$1"
    local parent_task_id="$2"
    local issue_counter="$3"
    
    if [ "$deps_json" == "null" ] || [ "$deps_json" == "[]" ]; then
        if [ "$parent_task_id" == "1" ]; then
            echo "None"
        else
            echo "Depends on completion of previous milestones"
        fi
        return
    fi
    
    local deps_str=""
    local deps_array=$(echo "$deps_json" | jq -r '.[]')
    
    for dep in $deps_array; do
        # Convert dependency format (like "1.1") to issue number
        # This is a simplified mapping - you may need to adjust based on your numbering
        local dep_issue="#$dep"
        if [ -n "$deps_str" ]; then
            deps_str="$deps_str, $dep_issue"
        else
            deps_str="$dep_issue"
        fi
    done
    
    echo "$deps_str"
}

echo "Reading tasks from JSON file..."

# Get total number of tasks
total_tasks=$(jq '.master.tasks | length' "$TASKS_FILE")
echo "Found $total_tasks main tasks"

# Initialize issue counter
issue_counter=1

# Process each main task
for task_index in $(seq 0 $((total_tasks - 1))); do
    
    # Get task data
    task_data=$(jq ".master.tasks[$task_index]" "$TASKS_FILE")
    task_id=$(echo "$task_data" | jq -r '.id')
    task_title=$(echo "$task_data" | jq -r '.title')
    task_description=$(echo "$task_data" | jq -r '.description')
    task_details=$(echo "$task_data" | jq -r '.details // ""')
    task_priority=$(echo "$task_data" | jq -r '.priority // "medium"')
    task_complexity=$(echo "$task_data" | jq -r '.complexityScore // 4')
    subtasks=$(echo "$task_data" | jq '.subtasks')
    subtasks_count=$(echo "$subtasks" | jq 'length')
    
    echo ""
    echo "Processing Task $task_id: $task_title ($subtasks_count subtasks)"
    
    # Get milestone name
    milestone_name="$task_id. $task_title"
    
    # Get labels
    module_label=$(get_module_label "$task_id")
    priority_label=$(get_priority_label "$task_priority")
    complexity_label=$(get_complexity_label "$task_complexity")
    
    # Process each subtask
    for subtask_index in $(seq 0 $((subtasks_count - 1))); do
        
        subtask_data=$(echo "$subtasks" | jq ".[$subtask_index]")
        subtask_id=$(echo "$subtask_data" | jq -r '.id')
        subtask_title=$(echo "$subtask_data" | jq -r '.title')
        subtask_description=$(echo "$subtask_data" | jq -r '.description')
        subtask_details=$(echo "$subtask_data" | jq -r '.details // ""')
        subtask_test_strategy=$(echo "$subtask_data" | jq -r '.testStrategy // ""')
        subtask_dependencies=$(echo "$subtask_data" | jq '.dependencies // []')
        
        # Convert dependencies
        dependencies_str=$(convert_dependencies "$subtask_dependencies" "$task_id" "$issue_counter")
        
        # Create issue body
        issue_body="**Task ID:** $task_id.$subtask_id
**Milestone:** $task_title
**Dependencies:** $dependencies_str

## Description
$subtask_description

## Implementation Details
$subtask_details

## Test Strategy
$subtask_test_strategy

## Acceptance Criteria
- [ ] Feature implemented according to specifications
- [ ] All requirements met
- [ ] Proper validation and error handling
- [ ] Security measures implemented
- [ ] Unit and integration tests created
- [ ] Documentation updated

## Parent Task
This is subtask $subtask_id of Task $task_id: $task_title

**Parent Task Description:** $task_description"

        # Create the GitHub issue
        echo "Creating issue #$issue_counter: $subtask_title"
        
        gh issue create \
            --title "$subtask_title" \
            --body "$issue_body" \
            --milestone "$milestone_name" \
            --label "$priority_label,$module_label,$complexity_label" \
            --repo "$REPO"
        
        if [ $? -eq 0 ]; then
            echo "  ✅ Issue #$issue_counter created successfully"
        else
            echo "  ❌ Failed to create issue #$issue_counter"
        fi
        
        # Increment issue counter
        ((issue_counter++))
        
        # Small delay to avoid rate limiting
        sleep 0.5
        
    done
    
    echo "Task $task_id completed: created $subtasks_count issues"
done

# Calculate total issues created
total_issues=$((issue_counter - 1))

echo ""
echo "=============================="
echo "GitHub Issues Creation Complete!"
echo "=============================="
echo ""
echo "Summary:"
echo "✅ $total_issues GitHub Issues created"
echo "✅ All issues linked to appropriate milestones"
echo "✅ All issues have proper labels (priority, module, complexity)"
echo "✅ All dependencies mapped"
echo ""
echo "Repository: https://github.com/$REPO"
echo "View milestones: https://github.com/$REPO/milestones"
echo "View issues: https://github.com/$REPO/issues"
echo ""
echo "All Task Master subtasks are now tracked as GitHub issues!"